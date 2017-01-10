package com.angeloparenteapp.italianrecipes.RecipesWidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class RecipesWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return new WidgetFactory(getApplicationContext(), intent);
    }
}
