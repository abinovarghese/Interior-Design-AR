package com.example.ardemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import android.support.design.widget.Snackbar;

public class MainActivity extends AppCompatActivity {



    private ArFragment fragment;

    private PointerDrawable pointer = new PointerDrawable();
    private boolean isTracking;
    private boolean isHitting;
    private Renderable renderer = null;
    private Node hitNode = null;
    //variable for counting two successive up-down events
    int clickCount = 0;
    //variable for storing the time of first click
    long startTime;
    //variable for calculating the total time
    long duration;
    //constant for defining the time duration between the click that can be considered as double-tap
    static final int MAX_DURATION = 500;

    private Context mContext;
    private Integer mPickedColor = android.graphics.Color.WHITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(view -> takePhoto());

        fragment = (ArFragment)
                getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);

        fragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {
            fragment.onUpdate(frameTime);
            onUpdate();
        });
        initializeGallery();


    }

    private void onUpdate() {
        boolean trackingChanged = updateTracking();
        View contentView = findViewById(android.R.id.content);
        if (trackingChanged) {
            if (isTracking) {
                contentView.getOverlay().add(pointer);
            } else {
                contentView.getOverlay().remove(pointer);
            }
            contentView.invalidate();
        }

        if (isTracking) {
            boolean hitTestChanged = updateHitTest();
            if (hitTestChanged) {
                pointer.setEnabled(isHitting);
               // Log.i("appclik","Hit Object");
                contentView.invalidate();
            }
        }
    }

    private boolean updateTracking() {
        Frame frame = fragment.getArSceneView().getArFrame();
        boolean wasTracking = isTracking;
        isTracking = frame != null &&
                frame.getCamera().getTrackingState() == TrackingState.TRACKING;
        return isTracking != wasTracking;
    }

    private boolean updateHitTest() {
        Frame frame = fragment.getArSceneView().getArFrame();
        android.graphics.Point pt = getScreenCenter();
        List<HitResult> hits;
        boolean wasHitting = isHitting;
        isHitting = false;
        if (frame != null) {
            hits = frame.hitTest(pt.x, pt.y);
            for (HitResult hit : hits) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                    isHitting = true;
                    break;
                }
            }
        }
        return wasHitting != isHitting;
    }

    private android.graphics.Point getScreenCenter() {
        View vw = findViewById(android.R.id.content);
        return new android.graphics.Point(vw.getWidth()/2, vw.getHeight()/2);
    }

    private void initializeGallery() {
        LinearLayout gallery = findViewById(R.id.gallery_layout);

//        ImageView andy = new ImageView(this);
//        andy.setImageResource(R.drawable.droid_thumb);
//        andy.setContentDescription("andy");
//        andy.setOnClickListener(view ->{addObject(Uri.parse("andy.sfb"));});
//        gallery.addView(andy);

//        ImageView cabin = new ImageView(this);
//        cabin.setImageResource(R.drawable.cabin_thumb);
//        cabin.setContentDescription("cabin");
//        cabin.setOnClickListener(view ->{addObject(Uri.parse("Cabin.sfb"));});
//        gallery.addView(cabin);
//
//        ImageView house = new ImageView(this);
//        house.setImageResource(R.drawable.house_thumb);
//        house.setContentDescription("house");
//        house.setOnClickListener(view ->{addObject(Uri.parse("House.sfb"));});
//        gallery.addView(house);
//
//        ImageView igloo = new ImageView(this);
//        igloo.setImageResource(R.drawable.igloo_thumb);
//        igloo.setContentDescription("igloo");
//        igloo.setOnClickListener(view ->{addObject(Uri.parse("igloo.sfb"));});
//        gallery.addView(igloo);

//        ImageView chair = new ImageView(this);
//        chair.setImageResource(R.drawable.droid_thumb);
//        chair.setContentDescription("chair");
//        chair.setOnClickListener(view ->{addObject(Uri.parse("chair.sfb"));});
//        gallery.addView(chair);

        ImageView chair2 = new ImageView(this);
        chair2.setImageResource(R.drawable.chair2_tn);
        chair2.setContentDescription("Chair2");
        chair2.setOnClickListener(view ->{addObject(Uri.parse("Chair2.sfb"));});
        gallery.addView(chair2);

        ImageView s = new ImageView(this);
        s.setImageResource(R.drawable.cc_tn);
        s.setContentDescription("1");
        s.setOnClickListener(view ->{addObject(Uri.parse("1.sfb"));});
        gallery.addView(s);


        ImageView oben = new ImageView(this);
        oben.setImageResource(R.drawable.fancy_tn);
        oben.setContentDescription("oben");
        oben.setOnClickListener(view ->{addObject(Uri.parse("oben sandalye.sfb"));});
        gallery.addView(oben);

        ImageView realsofa = new ImageView(this);
        realsofa.setImageResource(R.drawable.couch_tn);
        realsofa.setContentDescription("realsofa");
        realsofa.setOnClickListener(view ->{addObject(Uri.parse("Realistic sofa only.sfb"));});
        gallery.addView(realsofa);

//
    }

    private void addObject(Uri model) {
        Frame frame = fragment.getArSceneView().getArFrame();
        android.graphics.Point pt = getScreenCenter();
        List<HitResult> hits;
        if (frame != null) {
            hits = frame.hitTest(pt.x, pt.y);
            for (HitResult hit : hits) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                    placeObject(fragment, hit.createAnchor(), model);
                    break;

                }
            }
        }
    }

    private void placeObject(ArFragment fragment, Anchor anchor, Uri model) {
        CompletableFuture<Void> renderableFuture =
                ModelRenderable.builder()
                        .setSource(fragment.getContext(), model)
                        .build()
                        .thenAccept(renderable -> addNodeToScene(fragment, anchor, renderable))
                        .exceptionally((throwable -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage(throwable.getMessage())
                                    .setTitle("Codelab error!");
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            return null;
                        }));
    }

    private void addNodeToScene(ArFragment fragment, Anchor anchor, Renderable renderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);



        TransformableNode node = new TransformableNode(fragment.getTransformationSystem());

//        node.setOnTapListener(new Node.OnTapListener() {
//            @Override
//            public void onTap(HitTestResult hitTestResult, MotionEvent motionEvent) {
//                Log.d("OnTap", "handleOnTouch");
//                // First call ArFragment's listener to handle TransformableNodes.
//                fragment.onPeekTouch(hitTestResult, motionEvent);
//
//                //We are only interested in the ACTION_UP events - anything else just return
//                if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
//                    return;
//                }
//
//                // Check for touching a Sceneform node
//                if (hitTestResult.getNode() != null) {
//                    Log.d("OnTap", "handleOnTouch hitTestResult.getNode() != null");
//                    Node hitNode = hitTestResult.getNode();
//
//                    Toast.makeText(MainActivity.this, "Successfully Removed the object!!", Toast.LENGTH_SHORT).show();
//                    fragment.getArSceneView().getScene().removeChild(hitNode);
//                    //hitNode.getAnchor().detach();
//                    hitNode.setParent(null);
//                    hitNode = null;
//                }
//            }
//        });

        node.setOnTapListener(new Node.OnTapListener() {
            @Override
            public void onTap(HitTestResult hitTestResult, MotionEvent motionEvent) {

                renderer = renderable;
                Log.d("OnTap", "handleOnTouch");
                // First call ArFragment's listener to handle TransformableNodes.
                fragment.onPeekTouch(hitTestResult, motionEvent);

                //We are only interested in the ACTION_UP events - anything else just return
                if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
                    return;
                }

                if (hitTestResult.getNode() != null) {
                    Log.d("OnTap", "handleOnTouch hitTestResult.getNode() != null");
                    hitNode = hitTestResult.getNode();
                }


                final LinearLayout rl = findViewById(R.id.gallery_layout);

//                // Check for touching a Sceneform node
//                if (hitTestResult.getNode() != null) {
//                    Log.d("OnTap", "handleOnTouch hitTestResult.getNode() != null");
//
//                    // Get a GridView object from ColorPicker class
//                    GridView gv = (GridView) ColorPicker.getColorPicker(MainActivity.this);
//
//                    // Initialize a new AlertDialog.Builder object
//                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//
//                    // Set the alert dialog content to GridView (color picker)
//                    builder.setView(gv);
//
//                    // Initialize a new AlertDialog object
//                    final AlertDialog dialog = builder.create();
//
//                    // Show the color picker window
//                    dialog.show();
//
//                    // Set the color picker dialog size
//                    dialog.getWindow().setLayout(
//                            500,
//                            500);
//
//                    // Set an item click listener for GridView widget
//                    gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            // Get the pickedColor from AdapterView
//                            mPickedColor = (Integer) parent.getItemAtPosition(position);
//
//                            // Set the layout background color as picked color
//                            Button but = findViewById(R.id.button);
//                            but.setBackgroundColor(mPickedColor);
//                            renderable.getMaterial().setFloat4("baseColorTint", new Color(mPickedColor));
//                            //rl.setBackgroundColor(mPickedColor);
//                            Log.i("ColorPicker", mPickedColor.toString());
//                            // close the color picker
//                            dialog.dismiss();
//                        }
//                    });

                //}

            }
        });

        node.setRenderable(renderable);
        node.setParent(anchorNode);
        fragment.getArSceneView().getScene().addChild(anchorNode);
        node.select();
    }

    // Custom method to get the screen width in pixels
    private Point getScreenSize(){
        WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        //Display dimensions in pixels
        display.getSize(size);
        return size;
    }

    // Custom method to get status bar height in pixels
    public int getStatusBarHeight() {
        int height = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    private String generateFilename() {
        String date =
                new SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.getDefault()).format(new Date());
        return Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + File.separator + "Sceneform/" + date + "_screenshot.jpg";
    }

    private void saveBitmapToDisk(Bitmap bitmap, String filename) throws IOException {

        File out = new File(filename);
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(filename);
             ByteArrayOutputStream outputData = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputData);
            outputData.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            throw new IOException("Failed to save bitmap to disk", ex);
        }
    }

    private void takePhoto() {
        final String filename = generateFilename();
        ArSceneView view = fragment.getArSceneView();

        // Create a bitmap the size of the scene view.
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        // Make the request to copy.
        PixelCopy.request(view, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                try {
                    saveBitmapToDisk(bitmap, filename);
                } catch (IOException e) {
                    Toast toast = Toast.makeText(MainActivity.this, e.toString(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                        "Photo saved", Snackbar.LENGTH_LONG);
                snackbar.setAction("Open in Photos", v -> {
                    File photoFile = new File(filename);

                    Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                            MainActivity.this.getPackageName() + ".ar.codelab.name.provider",
                            photoFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW, photoURI);
                    intent.setDataAndType(photoURI, "image/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);

                });
                snackbar.show();
            } else {
                Toast toast = Toast.makeText(MainActivity.this,
                        "Failed to copyPixels: " + copyResult, Toast.LENGTH_LONG);
                toast.show();
            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));
    }

    private void changeColor(){

//        fragment.get
//        Renderable tintedRenderable = originalRenderable.makeCopy();
//        tintedRenderable.getMaterial().setFloat4("baseColorTint", new Color(1.0f, 0.0f, 0.0f, 1.0f));

    }

    private void handleOnTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
        Log.d("Deletion", "handleOnTouch");
        // First call ArFragment's listener to handle TransformableNodes.
        fragment.onPeekTouch(hitTestResult, motionEvent);

        //We are only interested in the ACTION_UP events - anything else just return
        if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
            return;
        }

        // Check for touching a Sceneform node
        if (hitTestResult.getNode() != null) {
            Log.d("Deletion", "handleOnTouch hitTestResult.getNode() != null");
            Node hitNode = hitTestResult.getNode();

            // if (hitNode.getRenderable() == andyRenderable) {
            Toast.makeText(MainActivity.this, "We've hit Renderable!!", Toast.LENGTH_SHORT).show();
            fragment.getArSceneView().getScene().removeChild(hitNode);

            // }
        }
    }

    public void changeColor(View view){
        Log.d("OnTap", "handleOnTouch hitTestResult.getNode() != null");

        // Get a GridView object from ColorPicker class
        GridView gv = (GridView) ColorPicker.getColorPicker(MainActivity.this);

        // Initialize a new AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Set the alert dialog content to GridView (color picker)
        builder.setView(gv);

        // Initialize a new AlertDialog object
        final AlertDialog dialog = builder.create();

        // Show the color picker window
        dialog.show();

        // Set the color picker dialog size
        dialog.getWindow().setLayout(
                500,
                500);

        // Set an item click listener for GridView widget
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the pickedColor from AdapterView
                mPickedColor = (Integer) parent.getItemAtPosition(position);

                // Set the layout background color as picked color
                Button but = findViewById(R.id.button);
                //but.setBackgroundColor(mPickedColor);
                if(renderer!= null) {
                    renderer.getMaterial().setFloat4("baseColorTint", new Color(mPickedColor));
                }
                //rl.setBackgroundColor(mPickedColor);
                Log.i("ColorPicker", mPickedColor.toString());
                // close the color picker
                renderer = null;
                dialog.dismiss();
            }
        });

    }

    public void changeTexture(View view){
        ShowDialog();

    }
    int progress1 = 0;
    int progress2 = 0;

    public void ShowDialog()
    {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        final View Viewlayout = inflater.inflate(R.layout.activity_dialog,
                (ViewGroup) findViewById(R.id.layout_dialog));

        final TextView item1 = (TextView)Viewlayout.findViewById(R.id.txtItem1); // txtItem1
        final TextView item2 = (TextView)Viewlayout.findViewById(R.id.txtItem2); // txtItem2
        item1.setText("Mettalic Factor");
        item2.setText("Roughness Factor");

        //popDialog.setIcon(android.R.drawable.);
        popDialog.setTitle("Please Select value 1-10 ");
        popDialog.setView(Viewlayout);

        //  seekBar1
        SeekBar seek1 = (SeekBar) Viewlayout.findViewById(R.id.seekBar1);
        seek1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                //Do something here with new value
               //item1.setText("Value of : " + progress);
                progress1 = progress;

            }

            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }
        });

        //  seekBar2
        SeekBar seek2 = (SeekBar) Viewlayout.findViewById(R.id.seekBar2);
        seek2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                //Do something here with new value
               //item2.setText("Value of : " + progress);
                progress2 = progress;
            }

            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }
        });


        // Button OK
        popDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(renderer!=null){
                            float val = progress1/10;
                            renderer.getMaterial().setFloat("metallic",val);
                            val = progress2/10;
                            renderer.getMaterial().setFloat("metallic",val);
                        }
                        dialog.dismiss();
                    }

                });


        popDialog.create();
        popDialog.show();

    }

    public void getMeasurement(View view){
        if(hitNode!=null) {
            Toast.makeText(MainActivity.this, "Successfully Removed the object!!", Toast.LENGTH_SHORT).show();
            fragment.getArSceneView().getScene().removeChild(hitNode);
            //hitNode.getAnchor().detach();
            hitNode.setParent(null);
            hitNode = null;
        }
    }

    double getMetersBetweenAnchors(Anchor anchor1, Anchor anchor2) {
        float[] distance_vector = anchor1.getPose().inverse()
                .compose(anchor2.getPose()).getTranslation();
        float totalDistanceSquared = 0;
        for(int i=0; i<3; ++i)
            totalDistanceSquared += distance_vector[i]*distance_vector[i];
        return Math.sqrt(totalDistanceSquared);
    }
}
