## Visualisation of pickup and delivery problem

This is a project that runs and visualises the meta heuristic algorithm called Adaptive Local Neighbourhood Search to solve a pickup and delivery problem.
A data file is provided with 3 vehicles and 7 calls that need to be allocated. If a call is not allocated to a vehicle, a 'dummy' vehicle will handle the call separately. There are constraints for time windows, capacity, and vehicle-call compatability. 

Each vehicle starts at its home node, and picks up and delivers cargo (blue containers) according to the route they have been assigned after the algorithm has been run.

# Overview of logic

A Read object is created to process the data file, creating numerous objects representing the different data provided. The read object is initiated only once in Main.java, and used throughout the project through the constructors of the JPanels.

The node locations in the frame are calculated in the CoordinateTransformer object, and are based on relative travel time between each node retrieved from the data set. The logic of this code is based on a python script written by INF273 group leader Eirik Petrie, as a result of me recieving help from him for this part of my project. 

A RunClickListener class handles the SOLVE button logic, and activates a method in a Run object that returns the completed solution. The solution is set as a field variable in MapPanel, which is used for the visualisation. A RouteManager is created which transforms the solution to a list of Route object, where the calls are assigned their correct pickup- and delivery nodes.

For each vehicle, an AnimationManager object is created, which creates a timer that animates the route by interpolating the line, and drawing a new part every time the timer ticks. I've drawn custom pixel-art vehicles and cargos, which go along with the animating line. 

Within the AnimationManager is a RouteRenderer object, that handles the drawing logic for each vehicle. The dummy vehicle has different logic, because it operates 'individually', and there is therefore a DummyRouteRenderer subclass that overrides the drawing logic methods that are different from the normal vehicles. 

I've created a generic class Tuple, that is used when i want to return two objects from a method.

# Disclaimer

All of the code written outside of the Visualisation folder is written previously by me during my course in INF273 this spring 2025.
The code has been slightly adapted to fit this project, e.g. the Run object. 
