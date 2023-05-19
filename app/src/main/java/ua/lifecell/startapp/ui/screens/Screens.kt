package ua.lifecell.startapp.ui.screens

class Screens {
}

sealed class Screen{
    object MainScreen : Screen(){
        const val ROUTE = "list"
    }
    object DetailsScreen : Screen(){
        const val ITEM_ID = "itemId"
        const val ITEM_NAME = "itemName"
        private const val ROUTE = "list_item_details"
        const val ROUTE_WITH_PARAMS = "$ROUTE?$ITEM_ID={$ITEM_ID}$$ITEM_NAME={$ITEM_NAME}"

        fun getRouteWithParams(id: String, name: String): String {
            return "$ROUTE?$ITEM_ID=$id$$ITEM_NAME=$name"
        }
    }
}