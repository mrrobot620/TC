package com.mrrobot.tc;

import android.accessibilityservice.AccessibilityService;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.Gson;


public class screenReaderService extends AccessibilityService {

    private HashMap<Integer, String> gridCodeMap = new HashMap<>();
    private int gridCodeCounter = 0;

    @Override
    public void onServiceConnected() {
        Log.wtf("this", "accessbility connected ");
        Toast.makeText(this, "Accessibility Connected", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo mNodeInfo = event.getSource();

        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            String packageName = event.getPackageName().toString();
            if ("com.android.chrome".equals(packageName)) {
                AccessibilityNodeInfo clickedNode = event.getSource();
                if (clickedNode != null) {
                    handleClickedNode(clickedNode);
                    clickedNode.recycle();
                }
            }
        }
    }


    public String GridLine(AccessibilityNodeInfo node) {
        StringBuilder resultBuilder = new StringBuilder();

        if (node == null) {
            return "";
        }

        // Process text within this node
        if (node.getText() != null && !node.getText().toString().isEmpty()) {
            String nodeText = node.getText().toString();
            resultBuilder.append(nodeText).append("\n");
        }

        // Traverse child nodes
        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo childNode = node.getChild(i);
            if (childNode != null) {
                resultBuilder.append(GridLine(childNode));
                childNode.recycle();
            }
        }

        return resultBuilder.toString();
    }


    @Override
    public void onInterrupt() {
        return;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Stopped", Toast.LENGTH_LONG).show();
    }

    private static String regexFinder(String pattern, String line) {
        String value = null;
        try {
            Pattern regexPattern = Pattern.compile(pattern);
            Matcher matcher = regexPattern.matcher(line);
            if (matcher.find()) {
                if (matcher.groupCount() >= 2) {
                    value = matcher.group(1) + " => " + matcher.group(2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }


    private void handleClickedNode(AccessibilityNodeInfo node) {
        String gridCode = GridLine(node);
        Log.d("XX", "Clicked Text:  " + gridCode);
        if (gridCode != null) {
            String real = regexFinder("\\| ([^\\s|]+) \\| [^\\s|]+ \\| ([^\\s|]+)", gridCode);
            saveGridCode(real);
            Log.d("XX", "Clicked Real:  " + real);
        }


    }

    private void saveGridCode(String code) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();

        // Increment the counter and use it as the key for the new grid code
        int key = gridCodeCounter++;

        // Save the grid code to SharedPreferences with the generated key
        editor.putString("grid_" + key, code);
        editor.putInt("grid_code_counter", gridCodeCounter);
        editor.apply();
    }


}




