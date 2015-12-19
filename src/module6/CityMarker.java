package module6;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import processing.core.PConstants;
import processing.core.PGraphics;

/** Implements a visual marker for cities on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * 
 */
public class CityMarker extends CommonMarker {
	
	public static int TRI_SIZE = 5;  // The size of the triangle marker
	
	public CityMarker(Location location) {
		super(location);
	}

	private int totalNumberOfQaukesNearby = 0;
    private float avgQuakesNearbyMagnitude = 0.0f;

    private boolean additionalInfoEnabled = false;


	public CityMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		// Cities have properties: "name" (city name), "country" (country name)
		// and "population" (population, in millions)
	}
	
	
	// pg is the graphics object on which you call the graphics
	// methods.  e.g. pg.fill(255, 0, 0) will set the color to red
	// x and y are the center of the object to draw. 
	// They will be used to calculate the coordinates to pass
	// into any shape drawing methods.  
	// e.g. pg.rect(x, y, 10, 10) will draw a 10x10 square
	// whose upper left corner is at position x, y
	/**
	 * Implementation of method to draw marker on the map.
	 */
	public void drawMarker(PGraphics pg, float x, float y) {
		//System.out.println("Drawing a city");
		// Save previous drawing style
		pg.pushStyle();
		
		// IMPLEMENT: drawing triangle for each city
		pg.fill(150, 30, 30);
		pg.triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
		
		// Restore previous drawing style
		pg.popStyle();
	}
	
	/** Show the title of the city if this marker is selected */
	public void showTitle(PGraphics pg, float x, float y)
	{
		String name = getCity() + " " + getCountry() + " ";
		String pop = "Pop: " + getPopulation() + " Million";
        String quakesNum = "Number of Quakes: " + totalNumberOfQaukesNearby;
        String avgMagnitude = String.format("Avg Magn.: %2.2f", avgQuakesNearbyMagnitude) ;

		pg.pushStyle();
		
		pg.fill(255, 255, 255);
		pg.textSize(12);
		pg.rectMode(PConstants.CORNER);

        float maxLength;
        float height;

        if (additionalInfoEnabled){
            maxLength =Math.max(Math.max(pg.textWidth(avgMagnitude),pg.textWidth(quakesNum)),
                    Math.max(pg.textWidth(name),pg.textWidth(pop)));
            height = 60;
        }
        else {
            maxLength = Math.max(pg.textWidth(name),pg.textWidth(pop));
            height = 35;
        }

        pg.rect(x, y - TRI_SIZE - height, maxLength + 6, height);
		pg.fill(0, 0, 0);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.text(name, x+3, y-TRI_SIZE-height + 2);
		pg.text(pop, x+3, y - TRI_SIZE - height + 16);

        if (additionalInfoEnabled) {
            pg.text(quakesNum, x+3, y - TRI_SIZE - height + 32);
            pg.text(avgMagnitude, x+3, y - TRI_SIZE - height + 46);
        }

		
		pg.popStyle();
	}
	
	private String getCity()
	{
		return getStringProperty("name");
	}
	
	private String getCountry()
	{
		return getStringProperty("country");
	}
	
	private float getPopulation()
	{
		return Float.parseFloat(getStringProperty("population"));
	}

    public void setTotalNumberOfQaukesNearby(int totalNumberOfQaukesNearby) {
        this.totalNumberOfQaukesNearby = totalNumberOfQaukesNearby;
    }

    public void setAvgQuakesNearbyMagnitude(float avgQuakesNearbyMagnitude) {
        this.avgQuakesNearbyMagnitude = avgQuakesNearbyMagnitude;
    }

    public void setAdditionalInfoEnabled(boolean additionalInfoEnabled) {
        this.additionalInfoEnabled = additionalInfoEnabled;
    }
}
