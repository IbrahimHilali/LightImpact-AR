package com.example.lightimpact;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {
    private ArFragment arFragment;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ge fragment
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        assert arFragment != null;
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();

            ModelRenderable
                    .builder()
                    .setSource(this, Uri.parse("model.sfb"))
                    .build()
                    .thenAccept(modelRenderable -> addModel(anchor, modelRenderable))
                    .exceptionally(throwable -> {
                        new AlertDialog
                                .Builder(this)
                                .setMessage(throwable.getMessage())
                                .show();
                        return null;
                    });
        });
    }

    private void addModel(Anchor anchor, ModelRenderable modelRenderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();
    }

}
