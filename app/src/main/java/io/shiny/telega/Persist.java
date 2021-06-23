package io.shiny.telega;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

public class Persist {
    public static Client client;
    public static boolean hasAuth;
    public static TdApi.AuthorizationState authorizationState;
}
