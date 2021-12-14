/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scandit.datacapture.searchandfindsample.models;

import com.scandit.datacapture.core.capture.DataCaptureContext;
import com.scandit.datacapture.core.source.Camera;

public final class DataCaptureManager {

    private static final String SCANDIT_LICENSE_KEY = "AXNAcQNpItGsKkzh2zlwJ+E98UiqHPrNajty+qZtRtfVUg3TxkWUwmp+OAYWG8g8zFsigo8gP/ELDiMmUEVYaixuWZ4fexibNgXp2dAttcY0b75kYEwZvht6/h6zYMnrvUtacqRC9f5JWSQo1UesNa5y6jPedHvzmXAhUtJesS5PTFy9NHMncGNQkeDDdwHwqUE+ltxFo84aPztuXULRGgNMDvlpYl3o9kYxoQ9JjayCUN7k6Fgs0Wx9vEepQIyDhmT41ygovR5WXWovoGQSKpB4F9RzfTyVJ3uY1mYnwc7pUbO2uVrE7QVXG3d6Q4r9Y2+BkxxQfs/WJxq/105K3XRuwle0XE85SGJAb8BEh8tYZ7j/J0PZ/cpLe9qbc0BleU4jl49og41TCL4o8nymWRZ8wU5BafiojmqoIQVrVmjZeby8IGBK8oFRFc3RSbyrMHwmsnpXwW5bFCJ9u3tNdx5ssjf/SuKkzlV6EtNeadFofTUk2E5MIAJPUpLgeuXhF0YX7hE3QehkQbWbyj8KIFI+5YdSr46XyaOGkaBH91uL9eDwYzpqQKf1R+iH1gy4354QmsSj1dmJ3P7QMdQ0gZeuaUgGVTo3QnJUCc/IGfjAW9WrppZfZxhU5ouN/FGXSq+IDWDbKUeUD2Ru1V5pxKe75+dPtdYvrFtiMQI9zIby9l6hOOZf5xuotv64QjvEZ2TnfhFHeO2vEqtDGX2OsSwvoOWgFgrGmohnfraImf40Sj54PylIwhfRIlbKMcVaTJliztJrHEj4tRTBGvvR1HGzve97/6Z8m3D4HARUH8kYzyK1XX6d0cORe52AZNQojSSgnCemMbf7zWnAN/gpuOKVGzV8n80O1XPFQoPBI4c00jtN0HsofskJSLKykg7HfC89lZMVHJl+chUXw4mOdHPRbIiEvqtu2rOvLdsE7/B7tAwUeBNpQWau8kf9C7xmYCYpYodek5DTWTmtp1W2DBWTvGmsd4PRXuPOaOdgFe9rMxOrwSLBefkhOgDl9fyp7kZPDM9KdD7N04ViDemwq7p+u1fIVY1WMIDSUPJySHoRHCnsz/5L7mxgsENtITdkvIemjUkamc6FTG4K4SKFw6vQo+7do57zzTzsl0GIODI9J3rDxZndTNVffj6D1TsH2QrMjbTmYOzRBxPe9EG8MvOte+3v6dooZdRnN8o/1XcuyOegFtPL/A/OPDVVhRTk";
    public static final DataCaptureManager CURRENT = new DataCaptureManager();
    public static final SearchDataCaptureManager SEARCH = new SearchDataCaptureManager();
    public static final FindDataCaptureManager FIND = new FindDataCaptureManager();

    final DataCaptureContext dataCaptureContext;
    public final Camera camera = Camera.getDefaultCamera();

    private DataCaptureManager() {
        // Create data capture context using your license key and set the camera as the frame source.
        dataCaptureContext = DataCaptureContext.forLicenseKey(SCANDIT_LICENSE_KEY);
        dataCaptureContext.setFrameSource(camera);
    }
}
