package com.marsool.firetool;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.GestureDescription;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.Collections;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import static android.content.ContentValues.TAG;
import static com.marsool.firetool.Settings.st;


public class GlobalActionBarService extends AccessibilityService {
    private static final String TAG = GlobalActionBarService.class
            .getSimpleName();
    boolean a,b,c,d,hope=true, please=true;
    public static boolean on=false;
    private FrameLayout mLayout;

    @Override
    protected boolean onGesture(int gestureId) {
        if(gestureId == GLOBAL_ACTION_BACK) {
            Log.d(TAG, "Gesture Home Pressed");
        }
        return super.onGesture(gestureId);
    }

    @Override
    protected void onServiceConnected() {
        // Create an overlay and display the action bar
        super.onServiceConnected();
        setServiceInfo();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.notificationTimeout = 100;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        this.setServiceInfo(info);
        on=true;
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        mLayout = new FrameLayout(this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        lp.format = PixelFormat.TRANSLUCENT;
        lp.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.TOP;
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.action_bar, mLayout);
        wm.addView(mLayout, lp);
        stop();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        setServiceInfo();
        if(st){
            event.getSource();
            if(event.getSource()!=null){
                if(a&&hope){
                    AccessibilityNodeInfo nodeInfo = event.getSource();
                    if (nodeInfo == null) {
                        return;
                    }
                        // We can find button using button name or button id
                    List<AccessibilityNodeInfo> list = nodeInfo
                            .findAccessibilityNodeInfosByText("التوصيل");
                    if(!list.isEmpty()){
                        list= Collections.singletonList(list.get(0));
                    }
                    for (AccessibilityNodeInfo node : list) {
                        configureSwipeButton();
                        break;

                    }

                        Log.i(TAG, "ACC::onAccessibilityEvent: nodeInfo=" + event.getEventType());
                }

                if(b&&please){
                    if (event.getSource() != null) {
                        if (event.getPackageName().equals("com.mrsool")) {
                            AccessibilityNodeInfo nodeInfo = event.getSource();


                            if (nodeInfo == null) {
                                return;
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                // We can find button using button name or button id
                                List<AccessibilityNodeInfo> list = nodeInfo
                                        .findAccessibilityNodeInfosByText("أنا مستعد أوصل هذا الطلب");

                                for (AccessibilityNodeInfo node : list) {
                                    AccessibilityNodeInfo parent= node.getParent();
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                }

                                list = nodeInfo.findAccessibilityNodeInfosByViewId("أنا مستعد أوصل هذا الطلب");
                                for (AccessibilityNodeInfo node : list) {
                                    AccessibilityNodeInfo parent= node.getParent();
                                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                }

                            }
                        }
                    }
                }

                if(c){
                    AccessibilityNodeInfo source = event.getSource();
                    if (source == null) {
                        return;
                    }
                    List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId("com.mrsool:id/edCost");
                    if (findAccessibilityNodeInfosByViewId.size() > 0) {
                        AccessibilityNodeInfo parent = (AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0);
                        // You can also traverse the list if required data is deep in view hierarchy.
                        String requiredText = parent.getText().toString();
                        Log.i("Required Text", requiredText);
                        AccessibilityNodeInfo nodeInf = event.getSource();
                        Log.i(TAG, "ACC::onAccessibilityEvent: nodeInfo=" + nodeInf);
                        if (nodeInf == null) {
                            return;
                        }
                            List<AccessibilityNodeInfo> lis = nodeInf
                                    .findAccessibilityNodeInfosByViewId("com.mrsool:id/edCost");
                            for (AccessibilityNodeInfo node : lis) {
                                Log.i(TAG, "ACC::onAccessibilityEvent: edCost " + node);
                                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }

                            lis = nodeInf
                                    .findAccessibilityNodeInfosByViewId("com.mrsool:id/edCost");
                            for (AccessibilityNodeInfo node : lis) {
                                Log.i(TAG, "ACC::onAccessibilityEvent: edCost " + node);
                                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                        Bundle bundle = new Bundle();
                        String firstInt = requiredText.replaceFirst(".*?(\\d+).*", "$1");
                        bundle.putCharSequence(parent.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, firstInt);

                        //set the text
                        source.performAction(parent.ACTION_SET_TEXT, bundle);
                        AccessibilityNodeInfo nodeInfo2 = event.getSource();
                        Log.i(TAG, "ACC::onAccessibilityEvent: nodeInfo2=" + nodeInfo2);
                        if (nodeInfo2 == null) {
                            return;
                        }
                        if(!d){
                            if (isNumeric(parent.getText().toString())){
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                        if(d){
                            if (isNumeric(parent.getText().toString())){
                                List<AccessibilityNodeInfo> list2 = nodeInfo2
                                        .findAccessibilityNodeInfosByViewId("com.mrsool:id/llSend");
                                for (AccessibilityNodeInfo node : list2) {
                                    Log.i(TAG, "ACC::onAccessibilityEvent: llsend " + node);
                                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }

                                list2 = nodeInfo2
                                        .findAccessibilityNodeInfosByViewId("com.mrsool:id/llsend");
                                for (AccessibilityNodeInfo node : list2) {
                                    Log.i(TAG, "ACC::onAccessibilityEvent: llsend " + node);
                                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }


                    }
                }
            }
        }



    }

    @Override
    public void onInterrupt() {

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void configureSwipeButton() {
        Path swipePath = new Path();
        swipePath.moveTo(500, 1500);
        swipePath.lineTo(500, 2300);
        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        gestureBuilder.addStroke(new GestureDescription.StrokeDescription(swipePath, 0, 500));
        dispatchGesture(gestureBuilder.build(), null, null);

    }
    private void stop() {
        Button swipeButton = (Button) mLayout.findViewById(R.id.swipe);
        swipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hope=!hope;
                Log.i(TAG,String.valueOf(hope));
            }
        });
    }
    public boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }
    public void setServiceInfo(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        a= pref.getBoolean("a", true);
        b= pref.getBoolean("b", true);
        c= pref.getBoolean("c", true);
        d= pref.getBoolean("d", true);
    }
}