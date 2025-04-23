package no.uib.inf101.test;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import no.uib.inf101.sample.parser.Read;
import no.uib.inf101.sample.visualisation.animation.AnimationManager;
import no.uib.inf101.sample.visualisation.animation.RouteRenderer;
import no.uib.inf101.sample.visualisation.components.LegendPanel;
import no.uib.inf101.sample.visualisation.components.MapPanel;
import no.uib.inf101.sample.visualisation.objects.Node;
import no.uib.inf101.sample.visualisation.objects.Route;


public class AnimationManagerTest {
    // create a mock for the MapPanel class
    Read read;
    AnimationManager animationManager;
    MapPanel mapPanel;
    LegendPanel legendPanel;

    public AnimationManagerTest(){
        try {
            read = new Read("src/main/resources/Call_7_Vehicle_3.txt");
            mapPanel = new MapPanel(read, null);
            legendPanel = new LegendPanel(read, mapPanel);
            mapPanel.legendPanel = legendPanel;
            animationManager = new AnimationManager(mapPanel.getRoutes().get(1), mapPanel::repaint, mapPanel);
        } catch (IOException e) {
            e.printStackTrace();
            read = null; // Handle initialization failure
        }
    }
    

    @Test
    void testSetRoute() {
        // Test that the route is set correctly
        Route route = new Route(Color.RED, Arrays.asList( // mock route
            new Node(0, 0, 0, false),
            new Node(1, 1, 1, false),
            new Node(2, 2, 2, false)
        ), 1);

        // Test that the route is initially empty
        assertEquals(animationManager.getRoute().nodes(), new ArrayList<>(), "Route should be empty before setting");

        // Set the route
        animationManager.setRoute(route);
        assertEquals(route, animationManager.getRoute(), "Route should be set correctly");
    }

    @Test
    void testStart() {
        // Test that the animation starts correctly
        animationManager.start();
        assertEquals(animationManager.getAnimationStep(), 0, "Animation step should be 0 after starting");
        assertEquals(animationManager.getCurrentNode(), 0, "Current node should be 0 after starting");
    }

    @Test
    void TestRouteRenderer() {
        RouteRenderer routeRenderer = new RouteRenderer(animationManager.getRoute(), mapPanel, animationManager);

        // Test that the route renderer is created correctly
        assertEquals(routeRenderer.getRoute(), animationManager.getRoute(), "Route renderer should be created with the correct route");

        // Test that the route renderer is not null
        assertNotNull(routeRenderer, "Route renderer should not be null");
    }
}