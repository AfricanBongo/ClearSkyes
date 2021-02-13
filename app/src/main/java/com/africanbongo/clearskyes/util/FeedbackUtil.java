package com.africanbongo.clearskyes.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Used to send feedback email using
 * <a href="https://rapidapi.com/fapi/api/fapimail/">Fapimail API</a>
 */
public final class FeedbackUtil {
    private static final String DEVELOPER_EMAIL = "\"dmtabvuri@gmail.com\"";
    private static final String API_KEY = "9444a4cc4emsh93f5287277cd663p12da72jsn8ba223c740db";
    private static final String SUBJECT_BASE = "Clearskyes Feedback: ";
    private static final String FROM_DEFAULT = "\"tadiwamtabvuri@gmail.com\"";
    private static final String FEEDBACK_FAILURE = "Couldn't send feedback";
    private static final String FEEDBACK_SENT = "Thank you for your feedback";

    // Value pair keys
    private static final String FROM = "\"sender\"";
    private static final String TO = "\"recipient\"";
    private static final String SUBJECT = "\"subject\"";
    private static final String TEXT = "\"message\"";

    /**
     * Send feedback to the developer of this awesome application.
     * If the feedback is compiled and being sent a {@link Toast} is displayed to the user.
     * Else if the feedback fails to be delivered a {@link Toast} is shown to the user,
     * alerting them of this error.
     * @param context {@link Context} used to display {@link Toast} to the user,
     * @param from {@link String} Optional, if not included post as null,
     * @param subject {@link String} subject of the feedback email,
     * @param body {@link String} body detailing the feedback from the user
     */
    public static void sendFeedback(Context context, @Nullable String from,
                                        @NonNull String subject, @NonNull String body) {
        if (from == null) {
            from = FROM_DEFAULT;
        }

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create("{" +
                TO + ": " + DEVELOPER_EMAIL + "," +
                FROM + ": "+ from + "," +
                SUBJECT + ": \""+ SUBJECT_BASE + subject +"\"," +
                TEXT + ": \"" + body + "\" " +
                "}", mediaType);

        Request request = new Request.Builder()
                .url("https://fapimail.p.rapidapi.com/email/send")
                .post(requestBody)
                .addHeader("content-type", "application/json")
                .addHeader("x-rapidapi-key", API_KEY)
                .addHeader("x-rapidapi-host", "fapimail.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, FEEDBACK_FAILURE, Toast.LENGTH_SHORT).show());
                Log.e(FeedbackUtil.class.getSimpleName(), e.getMessage(), e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });

        Toast.makeText(context, FEEDBACK_SENT, Toast.LENGTH_SHORT).show();
    }
}
