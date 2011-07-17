package tappem.marguerite.listeners;

import tappem.marguerite.FavoritesDbAdapter;
import tappem.marguerite.R;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FavoriteButtonListener implements OnClickListener {
	Context context;
	String stopId;
	String stopName;
	boolean isfavorite;
	public FavoriteButtonListener(Context context, String stopId,String stopName, boolean favorite)
	{
		this.context = context;
		this.stopId = stopId;
		this.stopName = stopName;
		isfavorite = favorite;
	}
	@Override
	public void onClick(View v) {
		FavoritesDbAdapter dbHelper = new FavoritesDbAdapter(context);
		//RelativeLayout favorite_holder = (RelativeLayout)v;
		//ImageView favorite_button = (ImageView) favorite_holder.findViewById(R.id.favoritestar_btn);
//		ImageView favorite_button = (ImageView) v;
		View favorite_button = v;
		dbHelper.open();
		if(isfavorite){
			favorite_button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.star_notfavorite));

			dbHelper.deleteFavorites(stopId);
			isfavorite=false;
		}
		else{//star_notfavorite




			long id = dbHelper.createFavorites(stopId, stopName);
			
			favorite_button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.star_favorite)); 
			isfavorite=true; 
		}
		dbHelper.close();

	}

}
