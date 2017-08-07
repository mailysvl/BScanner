package com.example.finalproject;

import android.util.Log;
import android.util.SparseArray;

import com.example.finalproject.camera.GraphicOverlay;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

/**
 * Created by ketmany on 19/03/2017.
 */

public class TextProcessor implements Detector.Processor<TextBlock> {

    private GraphicOverlay<ScannerGraphic> mGraphicOverlay;

    TextProcessor(GraphicOverlay<ScannerGraphic> scannerGraphicGraphicOverlay) {
        mGraphicOverlay = scannerGraphicGraphicOverlay;
    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections){
        mGraphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for(int i = 0; i < items.size(); i++){
            TextBlock item = items.valueAt(i);
            if(item != null && item.getValue() != null){
                Log.d("Processor", "Text detected! " + item.getValue());

            }
            ScannerGraphic graphic = new ScannerGraphic(mGraphicOverlay, item);
            mGraphicOverlay.add(graphic);
        }
    }
    @Override
    public void release(){
        mGraphicOverlay.clear();
    }
}
