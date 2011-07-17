package tappem.marguerite;

import java.util.ArrayList;

import tappem.marguerite.listeners.FavoriteButtonListener;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StopsEfficientAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<BusStop> buses;
	private boolean inFavorites = false;
	private Context context;
	private final int minListItems;
	private int resource;
	int[] busLineIconViewArray = {R.id.stopline1,R.id.stopline2,R.id.stopline3,R.id.stopline4,R.id.stopline5};
	int[] fbusLineIconViewArray = {R.id.f_stopline1,R.id.f_stopline2,R.id.f_stopline3,R.id.f_stopline4,R.id.f_stopline5};
	//private OnClickListener onclick;
	public StopsEfficientAdapter(Context context, ArrayList<BusStop> buses, int layoutResourceName) {
		mInflater = LayoutInflater.from(context);

		this.buses = buses;
		this.context = context;
		if(buses != null && buses.size() > 0)
			minListItems = buses.size();
		else 
			minListItems = 0;
		resource = layoutResourceName;
		if(resource == R.layout.favorites_stop_list)
			inFavorites = true;
	}

	public int getCount() {

		if(buses == null || (buses.size() < minListItems))
			return minListItems;
		else
			return buses.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(resource, null);
			//convertView.setClickable(true);

			holder = new ViewHolder();
			//	holder.busLine = (TextView) convertView.findViewById(R.id.busLine);
			if(inFavorites){
				holder.stopName = (TextView) convertView.findViewById(R.id.f_stopname);
				
			}else
				holder.stopName = (TextView) convertView.findViewById(R.id.stopname);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try{
			
			BusStop currentStop = buses.get(position);
			int index = 0;
			//System.out.println(currentStop.getStopLabel() + "   stop");

			int[] arrayLines = busLineIconViewArray;
			if(inFavorites){
				
//				ImageView favoriteBtn = (ImageView) convertView.findViewById(R.id.favoritestar_btn); 
//				favoriteBtn.setFocusable(true);
//				favoriteBtn.setClickable(true);
//				favoriteBtn.setOnClickListener(new FavoriteButtonListener(context, currentStop.getStopId(), currentStop.getStopLabel(), true));
				arrayLines = fbusLineIconViewArray;
			}
			
			
			
		


			for(BusLine line: currentStop.getBusLines())
			{
				////System.out.println("aqui?");
				if(index < arrayLines.length)
				{
					String imageName = line.getIcon();
					//	//System.out.println(imageName);
					if(!imageName.equals("")){
						ImageView busLineIcon = (ImageView) convertView.findViewById(arrayLines[index]);
						String uri = "drawable/" + imageName;
						int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());

						busLineIcon.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), imageResource));
						
						busLineIcon.setMaxWidth(24);
						
					}
				}
				index++;
			}



			holder.stopName.setText(currentStop.getStopLabel());


		}catch(Exception e){
			//System.out.println("Exception");
			holder.stopName.setText(" ");
//			if(inFavorites)
//				convertView.findViewById(R.id.favoritestar_btn).setVisibility(View.INVISIBLE);

		}

		//convertView.setOnClickListener(context);

		return convertView;
	}

	static class ViewHolder {
		TextView stopName;
		//RelativeLayout favoriteBtn;
	}
}
