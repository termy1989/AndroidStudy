package ru.oepak22.githubmvp.api;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import android.content.Context;


import ru.oepak22.githubmvp.AppDelegate;

// класс-определитель запросов, ответы на которые должны быть подменены
public class RequestsHandler {

    private final Map<String, String> mResponsesMap = new HashMap<>();

    public RequestsHandler() {
        mResponsesMap.put("/user", "auth.json");
        mResponsesMap.put("/user/repos", "repositories.json");
    }

    public boolean shouldIntercept(@NonNull String path) {
        Set<String> keys = mResponsesMap.keySet();
        for (String interceptUrl : keys) {
            if (path.contains(interceptUrl)) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    public Response proceed(@NonNull Request request, @NonNull String path) {
        String token = RepositoryProvider.provideKeyValueStorage().getToken();
        if ("error".equals(token)) {
            return OkHttpResponse.error(request, 400, "Error for path " + path);
        }

        Set<String> keys = mResponsesMap.keySet();
        for (String interceptUrl : keys) {
            if (path.contains(interceptUrl)) {
                String mockResponsePath = mResponsesMap.get(interceptUrl);
                return createResponseFromAssets(request, mockResponsePath);
            }
        }

        return OkHttpResponse.error(request, 500, "Incorrectly intercepted request");
    }

    @NonNull
    private Response createResponseFromAssets(@NonNull Request request, @NonNull String assetPath) {
        Context context = AppDelegate.getContext();
        try {
            final InputStream stream = context.getAssets().open(assetPath);
            //noinspection TryFinallyCanBeTryWithResources
            try {
                return OkHttpResponse.success(request, stream);
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            return OkHttpResponse.error(request, 500, e.getMessage());
        }
    }
}
