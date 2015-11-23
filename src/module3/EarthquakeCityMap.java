package module3;

//Java utilities libraries

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//import java.util.Collections;
//import java.util.Comparator;
//Processing library
//Unfolding libraries
//Parsing library

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
    private ArrayList<SimplePointMarker> markers;
    private float HEIGHT_START_MAP = 50;
    private float WIDTH_START_MAP = 200;

	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	private void getProperties() {
        Properties prop = new Properties();
        Reader propertyReader = null;
        JSONObject jsonInput;

        String PATH_TO_PROPERTY_FILE = "applicationData.properties";

        String PATH_TO_JSON_FILE = "applicationProperties.json";

        try {
            propertyReader = createReader(PATH_TO_PROPERTY_FILE);
            jsonInput = loadJSONObject(PATH_TO_JSON_FILE);

            // load a properties file

            prop.load(propertyReader);

            // get the property value and print it out
            System.out.println(prop.getProperty("my"));
            System.out.println(jsonInput.getString("my", "Error"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (propertyReader != null) {
                try {
                    propertyReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setup() {

        getProperties();

		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, WIDTH_START_MAP, HEIGHT_START_MAP,
                    700, 500, new MBTilesMapProvider(mbTilesString));

            earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, WIDTH_START_MAP, HEIGHT_START_MAP,
                    700, 500, new Google.GoogleMapProvider());

			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers

	    markers = new ArrayList<>();


	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
        int earthquakesCounter = 0;

	    if (earthquakes.size() > 0) {

            for (PointFeature quake : earthquakes) {

                markers.add(earthquakesCounter, createMarker(quake));
                earthquakesCounter += 1;
                // PointFeatures also have a getLocation method
            }

	    }
	    
	    // Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    // int yellow = color(255, 255, 0);

	}
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	public SimplePointMarker createMarker(PointFeature quake)
	{
		// finish implementing and use this method, if it helps.
        MagnitudeScale magType;
        // System.out.println(quake.getProperties());
        Object magObj = quake.getProperty("magnitude");
        float mag = Float.parseFloat(magObj.toString());

        if (mag < THRESHOLD_LIGHT) {
            magType = MagnitudeScale.MINOR;
        }
        else if ( (mag>=THRESHOLD_LIGHT) && (mag<THRESHOLD_MODERATE)) {
            magType = MagnitudeScale.MEDIUM;
        }
        else {
            magType = MagnitudeScale.MAJOR;
        }

        SimplePointMarker marker = new SimplePointMarker(quake.getLocation());

        marker.setColor(magType.getColor());
        marker.setRadius(magType.getSize());

		return marker;
	}
	
	public void draw() {
	    background(10);
	    map.draw();
        markers.forEach(map::addMarker);
	    addKey();

	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{
        // Remember you can use Processing's graphics methods here
        float START_POS_X = 20;
        float START_POS_Y = HEIGHT_START_MAP;
        float LEGEND_WIDTH = WIDTH_START_MAP - START_POS_X - 10;
        float LEGEND_HEIGHT = 300;


        fill(color(255, 255, 255));
        rect(START_POS_X,START_POS_Y,LEGEND_WIDTH,LEGEND_HEIGHT);


        float CIRCLE_START_POS_X = (float) (START_POS_X+LEGEND_WIDTH*0.2);
        float TEXT_START_POS_X = CIRCLE_START_POS_X + 15;
        float POS_Y = START_POS_Y + 30;

        int black = color(0,0,0);

        fill(black);
        textSize(14);
        text("Earthquake Key", CIRCLE_START_POS_X, POS_Y);

        textSize(12);
        for (MagnitudeScale scaleProperty : MagnitudeScale.values()) {

            POS_Y = POS_Y + 50;

            fill(scaleProperty.getColor());

            ellipse(CIRCLE_START_POS_X,
                    POS_Y,
                    scaleProperty.getSize(),
                    scaleProperty.getSize());

            fill(black);
            text(scaleProperty.getDescription(),
                    TEXT_START_POS_X,
                    POS_Y + scaleProperty.getSize()/2);
        }
	
	}
}
