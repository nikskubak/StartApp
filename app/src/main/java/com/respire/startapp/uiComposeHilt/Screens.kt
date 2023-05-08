package com.respire.startapp.uiComposeHilt

class Screens {
}

sealed class Screen{
    object MainScreen : Screen(){
        const val ROUTE = "list"
    }
    object DetailsScreen : Screen(){
        const val ITEM_ID = "itemId"
        const val ROUTE = "list_item_details"
        const val ROUTE_WITH_PARAMS = "$ROUTE/{${ITEM_ID}}"
    }
}