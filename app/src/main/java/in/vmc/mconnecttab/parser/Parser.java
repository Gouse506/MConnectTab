package in.vmc.mconnecttab.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.vmc.mconnecttab.model.VisitData;
import in.vmc.mconnecttab.utils.TAG;

/**
 * Created by mukesh on 11/3/16.
 */
public class Parser implements TAG {


    private static String code, message;
    private static JSONArray data;

    public synchronized static ArrayList<VisitData> ParseResponse(JSONObject response) throws JSONException {
        ArrayList<VisitData> VisitData = new ArrayList<VisitData>();
        if (response.has(CODE)) {
            String code = response.getString(CODE);
        }
        if (response.has(MESSAGE)) {
            String message = response.getString(MESSAGE);
        }
        if (response.has(DATA)) {
            data = response.getJSONArray(DATA);
            for (int i = 0; i < data.length(); i++) {
                JSONObject currentvisit = data.getJSONObject(i);
                VisitData visitData = new VisitData();
                visitData.setCode(response.getString(CODE));
                visitData.setMessage(response.getString(MESSAGE));

                if (currentvisit.has(SITENAME)) {
                    visitData.setSitename(currentvisit.getString(SITENAME));
                }
                if (currentvisit.has(SITEID)) {
                    visitData.setSiteid(currentvisit.getString(SITEID));
                }
                if (currentvisit.has(SITEDESC)) {
                    visitData.setSitedesc(currentvisit.getString(SITEDESC));
                }
                if (currentvisit.has(BID)) {
                    visitData.setBid(currentvisit.getString(BID));
                }
                if (currentvisit.has(SITEICON)) {
                    visitData.setSiteicon(currentvisit.getString(SITEICON));
                }
                if (currentvisit.has(NUMBER)) {
                    visitData.setNumber(currentvisit.getString(NUMBER));
                }

                if (currentvisit.has(OFFER_PER)) {
                    visitData.setOffer(currentvisit.getString(OFFER_PER));

                }
                if (currentvisit.has(PROPERTY_NAME)) {
                    visitData.setPropertyname(currentvisit.getString(PROPERTY_NAME));
                }
                if (currentvisit.has(OFFER_DEC)) {
                    visitData.setOffer_desc(currentvisit.getString(OFFER_DEC));
                }
                if (currentvisit.has(LIKES)) {
                    String Like = currentvisit.getString(LIKES);
                    if (Like.equals("1")) {
                        visitData.setLike(true);
                    } else {
                        visitData.setLike(false);
                    }
                }
                VisitData.add(visitData);

            }
        } else {
            VisitData visitData = new VisitData();
            visitData.setCode(response.getString(CODE));
            visitData.setMessage(response.getString(MESSAGE));
            VisitData.add(visitData);
        }

        return VisitData;
    }
}
