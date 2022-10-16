package com.example.sceneview_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.Plane;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private boolean isModelRendered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdate);
    }

    private void onUpdate(FrameTime frameTime) {

        if(isModelRendered)
            return;

        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<Plane> planes = frame.getUpdatedTrackables(Plane.class);

        for (Plane plane : planes){

            if (plane.getTrackingState() == TrackingState.TRACKING){
                Anchor anchor = plane.createAnchor(plane.getCenterPose());
                makeCube(anchor);
                break;
            }
        }
    }

    private void makeCube(Anchor anchor) {

        isModelRendered = true;

        MaterialFactory
                .makeOpaqueWithColor(this, new Color(android.graphics.Color.CYAN))
                .thenAccept(material -> {
                    ModelRenderable cubeRenderable = ShapeFactory
                            .makeCube(new Vector3(0.3f,0.3f,0.3f),
                                        new Vector3(0.3f,0.3f,0),
                                        material);

                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setRenderable(cubeRenderable);
                    arFragment.getArSceneView().getScene().addChild(anchorNode);
                })
        ;
    }
}