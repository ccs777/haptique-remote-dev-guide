Haptique Remote App – Developer Guide

This guide helps you integrate the Haptique Remote with your Android 12+ app. The remote features:

24 physical buttons with standard and custom keycodes.
An RGB light ring with customizable color and animation effects.
🔑 Key Event Handling

To handle hardware key events from the remote, use the onKey() method via View.OnKeyListener.

📌 Implementation Steps
Make sure the target view is focusable and has input focus.
Attach an OnKeyListener:
yourView.setOnKeyListener(new View.OnKeyListener() {
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        // Your logic here
        return true;
    }
});
Respond to specific key codes.
🎮 Supported Key Codes
Key Code	Label	Function
F1	Power	Power On/Off
F2	Home	Home Screen
F5	Voice	Voice Input
VOLUME_UP	Page Up	Scroll Up
VOLUME_DOWN	Page Down	Scroll Down
DPAD_UP	Up	Navigate Up
DPAD_DOWN	Down	Navigate Down
DPAD_LEFT	Left	Navigate Left
DPAD_RIGHT	Right	Navigate Right
DPAD_CENTER	OK	Confirm Selection
PAGE_UP	Volume Up	Increase Volume
PAGE_DOWN	Volume Down	Decrease Volume
BACK	Back	Return to Previous
MENU	Menu	Open Menu
VOLUME_MUTE	Mute	Mute/Unmute
MEDIA_REWIND	Rewind	Rewind Playback
MEDIA_PLAY_PAUSE	Play/Pause	Toggle Playback
MEDIA_FAST_FORWARD	Fast Forward	Forward Playback
F6 to F11	Custom	Developer-defined
✅ Best Practices
Always ensure the view has focus (view.setFocusable(true) and requestFocus()).
Handle KeyEvent.ACTION_DOWN and ACTION_UP separately if needed.
Avoid overriding essential system buttons unless required.
Use key events at the appropriate level in the view hierarchy.
🌈 RGB Light Ring Control

The Haptique Remote includes 8 individually addressable RGB lights with support for:

Static color settings
Dynamic patterns
Directional animations (e.g., clockwise rotation)
📁 Setup
Include the RGBLight.java file in your Android project.
(A download link can be added here if you're hosting the file)

🔧 Control Commands
RGBLights.getInstance().turnOn();    // Turns on the lights
RGBLights.getInstance().turnOff();   // Turns off the lights
RGBLights.getInstance().ctrl(...);   // Controls color/pattern per light
🎨 Example – Set a Single Color
Turn the first light red:

RGBLights.getInstance().ctrl(0xFF0000, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00);
Set all 8 lights red:

RGBLights.getInstance().ctrl(
    0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000,
    0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000
);
🌀 Example – Clockwise Animation
Coming soon – the SDK will support built-in animation helpers for clockwise effects and color cycles. You can manually sequence the ctrl() calls in a timer/handler loop for now.

📣 Contribution / Contact

This guide is part of the Haptique Developer Tools. If you'd like to contribute or get early access to the SDK and demo apps:

📧 Email: willis@cantatacs.com
🌐 Website: https://www.cantatacs.com
🛠️ Coming soon: GitHub repo with SDK and samples
