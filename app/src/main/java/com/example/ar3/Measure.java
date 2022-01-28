package com.example.ar3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class Measure extends AppCompatActivity {

    private AnchorNode currentAnchorNode;
    private Anchor currentAnchor = null;
    private AnchorNode previousAnchorNode = null;
    private Anchor previousAnchor = null;
    private TransformableNode currenTransformableNode = null;
    private TransformableNode previousTransformableNode = null;
    private Button clearall;

    ArFragment arFragment1;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.measure);
        Button mButton = findViewById(R.id.measure);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if ((currenTransformableNode != null) && (previousAnchorNode != null)) {
                    Vector3 difference = Vector3.subtract(currenTransformableNode.getWorldPosition(), previousTransformableNode.getWorldPosition());
                    Toast.makeText(getBaseContext(),"Distance Between the Two objects is : " + difference.length()  + " metres", Toast.LENGTH_LONG).show();
                }
            }
        });
        clearall = findViewById(R.id.clearall);
        clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (currentAnchorNode != null ) {
                    clearAnchor();
                }
            }
        });

        arFragment1 = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment1);
        assert arFragment1 != null;
        arFragment1.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (previousAnchorNode != null) {
                        Toast.makeText(getBaseContext()," Clear All before placing new objects", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (currentAnchorNode != null) {
                        previousAnchorNode = currentAnchorNode;
                        previousAnchor = currentAnchor;
                    }
                    //if (plane.getType() != Plane.Type.HORIZONTAL_UPWARD_FACING) return;
                    Anchor anchor = hitResult.createAnchor();
                    placeObject(arFragment1, anchor, R.raw.sphere);
                }
        );
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void placeObject(ArFragment arFragment, Anchor anchor, int uri) {
        ModelRenderable.builder()
                .setSource(arFragment.getContext(), uri)
                .build()
                .thenAccept(modelRenderable -> addNodeToScene(arFragment, anchor, modelRenderable))
                .exceptionally(throwable -> {
                    Toast.makeText(arFragment.getContext(), "Sorry,Something went wrong", Toast.LENGTH_LONG).show();
                    return null;
                });
    }
    private void addNodeToScene(ArFragment arFragment, Anchor anchor, Renderable renderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        node.select();
        currentAnchor = anchor;
        currentAnchorNode = anchorNode;
        previousTransformableNode = currenTransformableNode;
        currenTransformableNode = node;
    }

    private void clearAnchor() {
        currentAnchor = null;
        if (currentAnchorNode != null) {
            arFragment1.getArSceneView().getScene().removeChild(currentAnchorNode);
            currentAnchorNode.getAnchor().detach();
            currentAnchorNode.setParent(null);
            currentAnchorNode = null;
            arFragment1.getArSceneView().getScene().removeChild(previousAnchorNode);
            previousAnchorNode.getAnchor().detach();
            previousAnchorNode.setParent(null);
            previousAnchorNode = null;
        }
    }
}



