/**
 * Copyright (c) 2015 Rareloop Ltd
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.rareloop.cordova.appversion;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import android.util.TypedValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageManager;

import android.util.Log;

/**
 * Cordova plugin that allows for an arbitrarly sized and positioned WebView to be shown ontop of the canvas
 */
public class RareloopAppVersion extends CordovaPlugin {

    private static final String TAG = "RareloopAppVersion";

    /**
     * Executes the request and returns PluginResult
     *
     * @param  action          
     * @param  args            
     * @param  callbackContext 
     * @return boolean                
     * @throws JSONException   
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        /**
         * appVersion
         */
        if (action.equals("getAppVersion")) {
            try {            
				JSONObject response = new JSONObject();
				response = extractVersionInfoIntoJson(response);
				response = extractVersionInfoIntoJson("com.google.android.webview", response);
				response = extractVersionInfoIntoJson("asia.sharelike.jpos", response);
                callbackContext.success(response);
            } catch (NameNotFoundException e) {
                callbackContext.error("Exception thrown");
            }

            return true;
        }

        // Default response to say the action hasn't been handled
        return false;
    }

	private JSONObject extractVersionInfoIntoJson(JSONObject attachJsonObject) throws NameNotFoundException {
		PackageManager packageManager = this.cordova.getActivity().getPackageManager();
		String packName = this.cordova.getActivity().getPackageName();
		System.out.println("packName: " + packName);
		PackageInfo packInfoTmp = new PackageInfo();
		try {
			packInfoTmp = packageManager.getPackageInfo(packName, 0);
		} catch (NameNotFoundException e) {
			packInfoTmp.versionName = "9999.0.0";
			packInfoTmp.versionCode = 999999999;
		}

		try {
			attachJsonObject.put("version", packInfoTmp.versionName);
			attachJsonObject.put("build", packInfoTmp.versionCode);
		} catch(Exception e) {
			throw new NameNotFoundException();
		}
		return attachJsonObject;
	}

	private JSONObject extractVersionInfoIntoJson(String packName, JSONObject attachJsonObject) throws NameNotFoundException {
		String[] splitedPackName = packName.split("\\.");
		String appName = splitedPackName[splitedPackName.length - 1];
		PackageManager packageManager = this.cordova.getActivity().getPackageManager();
		PackageInfo packInfoTmp = new PackageInfo();
		try {
			packInfoTmp = packageManager.getPackageInfo(packName, 0);
		} catch (NameNotFoundException e) {
			packInfoTmp.versionName = "9999.0.0";
			packInfoTmp.versionCode = 999999999;
		}
		try {
			attachJsonObject.put(appName + "_version", packInfoTmp.versionName);
			attachJsonObject.put(appName + "_build", packInfoTmp.versionCode);
		} catch(Exception e) {
			throw new NameNotFoundException();
		}
		return attachJsonObject;
	}
}

