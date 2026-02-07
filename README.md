# Interior Design AR

An Android app that lets you place and customize 3D furniture in your real-world space using Augmented Reality. Built with Google ARCore and Sceneform.

## What It Does

Point your camera at a flat surface, pick a piece of furniture, and drop it into the scene. Move it around, resize it, rotate it -- and tweak colors and material properties in real time. When you're happy with the layout, snap a photo and share it.

### Features

- **AR surface detection** -- Automatically detects floors, tables, and other flat surfaces using ARCore plane tracking
- **3D furniture catalog** -- Browse and place chairs, sofas, and other furniture from a scrollable gallery
- **Real-time manipulation** -- Move, rotate, and scale placed objects using touch gestures (powered by Sceneform's TransformableNode)
- **Color customization** -- Tap a placed object and pick a new color from an HSV color palette
- **Material tuning** -- Adjust metallic and roughness factors via sliders for realistic material rendering
- **Photo capture** -- Take screenshots of your AR scene using PixelCopy and share them directly
- **Object removal** -- Tap to select and delete objects you no longer want in the scene

## Architecture

```
MainActivity
  |
  |-- WritingArFragment        Custom ArFragment with storage permissions
  |-- PointerDrawable          Visual crosshair indicator for surface tracking
  |-- ColorPicker              HSV color palette grid for real-time color changes
  |
  |-- ARCore                   Plane detection, hit testing, anchor management
  |-- Sceneform                3D model rendering, TransformableNode interactions
  |-- PixelCopy                AR scene screenshot capture
```

### How It Works

1. **Surface Detection** -- ARCore continuously tracks camera frames and detects horizontal planes. A green dot indicates a valid placement surface; a gray "X" means no surface found.
2. **Object Placement** -- When you select furniture, the app performs a hit test at screen center, creates an Anchor on the detected plane, and attaches a 3D model via Sceneform's `ModelRenderable`.
3. **Interaction** -- Each object is wrapped in a `TransformableNode` enabling pinch-to-scale, drag-to-move, and twist-to-rotate. Tapping an object selects it for color/material editing.
4. **Customization** -- Color changes are applied by modifying the `baseColorTint` material property. Material sliders adjust `metallic` and `roughness` float parameters on the Sceneform renderable.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| AR Engine | Google ARCore 1.7 |
| 3D Rendering | Google Sceneform 1.7 |
| Language | Java 8 |
| Platform | Android SDK 28 (min SDK 24) |
| 3D Models | OBJ/FBX with SFA/SFB pipeline |

## Getting Started

### Prerequisites

- Android Studio 3.1+
- Android device with [ARCore support](https://developers.google.com/ar/devices)
- Android 7.0 (API 24) or higher

### Build & Run

```bash
git clone https://github.com/abinovarghese/Interior-Design-AR.git
cd Interior-Design-AR/ARDemo
```

Open the `ARDemo` folder in Android Studio, sync Gradle, and run on an ARCore-supported device.

A pre-built APK is available at `ARDemo/app/release/app-release.apk`.

## Project Structure

```
ARDemo/
  app/
    src/main/java/com/example/ardemo/
      MainActivity.java          # Core AR logic, object placement, UI controls
      WritingArFragment.java     # AR fragment with storage permission handling
      ColorPicker.java           # HSV color grid for material customization
      PointerDrawable.java       # Crosshair overlay for surface tracking feedback
    src/main/res/
      layout/activity_main.xml   # Main UI: AR view, furniture gallery, FABs
      layout/activity_dialog.xml # Material property sliders dialog
    sampledata/                  # 3D models (OBJ, FBX, SFA) and textures
```
