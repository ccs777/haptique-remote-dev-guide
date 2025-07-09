package com.cantata.remote.source.rgbLights;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class RGBLights {
    /**文件接口地址 File interface address*/
    private static final String FILE_PATH = "/dev/rgb_light";
    private static RGBLights instance;
    /**当前操作的RGB灯数据  RGB light data for the current operation*/
    private final byte[] mGRBBuff;
    /**缓存上次控制的RGB灯数据，但不缓存RGB灯关的数据
     * Cache the last controlled RGB light data, but do not cache the RGB light off data*/
    private final byte[] mGRBCaches;
    private RandomAccessFile mFileStream;

    public static synchronized RGBLights getInstance(){
        if(instance == null){
            instance = new RGBLights();
        }
        return instance;
    }

    private RGBLights(){
        mGRBBuff = new byte[24];
        mGRBCaches = new byte[24];
        for(int i = 0; i < 8; i++){
            setRgb(i, 0xFF0000); //all red
        }
        System.arraycopy(mGRBBuff, 0, mGRBCaches, 0, mGRBCaches.length); //给开灯一个初始化颜色，全红色  Give the lights an initial color, all red
    }

    private synchronized void initFileStream() throws FileNotFoundException {
        if(mFileStream == null){
            mFileStream = new RandomAccessFile(FILE_PATH, "rw");
        }
    }

    /**
     * Set the GRB color value of the corresponding position light
     * @param index Light position controller
     * @param rgb RGB color
     */
    private void setRgb(int index, int rgb){
        if(index < 0 || index > 7){
            throw new IllegalArgumentException("invalid index:" + index);
        }

        int st = index * 3;
        mGRBBuff[st] = (byte)((rgb >> 8) & 0xff);
        mGRBBuff[st + 1] = (byte)((rgb >> 16) & 0xff);
        mGRBBuff[st + 2] = (byte)(rgb & 0xff);
    }


    /**
     * Gets the color cache of the current light, which has no light off state
     * @return
     */
    public int[] getCurrentRgbCaches() {
        int[] rgbValues = new int[8];
        for (int i = 0; i < 8; i++) {
            int st = i * 3;
            int red = mGRBCaches[st + 1] & 0xFF;  // Red is stored in index + 1
            int green = mGRBCaches[st] & 0xFF;    // Green is stored in index
            int blue = mGRBCaches[st + 2] & 0xFF; // Blue is stored in index + 2
            // Combine the colors into a single integer
            rgbValues[i] = (red << 16) | (green << 8) | blue;
        }
        Log.e("RGBLights",""+ Arrays.toString(rgbValues));
        return rgbValues;
    }


    /**
     * 向RGB灯接口写入颜色，同时缓存写入的颜色，如果是关闭RGB灯命令，数据全是0，不会存入缓存。
     * The color is written to the RGB indicator interface, and the written color is cached.
     * If the RGB indicator command is turned off, the data is all 0 and will not be stored in the cache.
     * @throws Exception
     */
    private void writeData() throws Exception{
        initFileStream();
        mFileStream.write(mGRBBuff);
        boolean isCache = false;
        for(byte b : mGRBBuff){
            if(b != 0) {
                isCache = true;
                break;
            }
        }
        if(isCache){
            System.arraycopy(mGRBBuff, 0, mGRBCaches, 0, mGRBBuff.length);
        }
    }

    /**
     *Control eight light colors
     */
    public void ctrl(int rgb0, int rgb1, int rgb2, int rgb3, int rgb4, int rgb5, int rgb6, int rgb7){
        try{
            setRgb(0, rgb0);
            setRgb(1, rgb1);
            setRgb(2, rgb2);
            setRgb(3, rgb3);
            setRgb(4, rgb4);
            setRgb(5, rgb5);
            setRgb(6, rgb6);
            setRgb(7, rgb7);
            writeData();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Store a color, set 8 RGB lights to the same color
     * @param rgb
     */
    public void ctrl(int rgb){
        ctrl(rgb, rgb, rgb, rgb, rgb, rgb, rgb, rgb);
    }

    /**
     * Controls the color of a light in a position，默认diyife
     * @param index
     * @param rgb
     */
    public void ctrl(int index, int rgb){
        try{
            setRgb(index, rgb);
            writeData();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Turn on 8 RGB lights, the color turned on is the last color to control RGB
     */
    public void turnOn(){
        try{
            System.arraycopy(mGRBCaches, 0, mGRBBuff, 0, mGRBBuff.length);
            writeData();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Turn off RGB light
     */
    public void turnOff(){
        try{
            initFileStream();
            for(int i = 0; i < 8; i++){
                setRgb(i, 0x00);
            }
            mFileStream.write(mGRBBuff);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * When you close the app, release RGBLights
     */
    public static synchronized void release(){
        if(instance != null){
            try{
                instance.turnOff();
                if(instance.mFileStream != null){
                    instance.mFileStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            instance = null;
        }
    }
}
