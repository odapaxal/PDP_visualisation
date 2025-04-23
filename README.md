# TO DO
- [x] Import project 
- [x] Cleanup project
- [x] Make visualisation file structure 
- [x] Create node map panel
- [x] Plot node map
- [x] Draw lines from solution
- [x] Make visualisation 
- [x] Make solution button
- [x] Make output message
- [x] Make dummy route visualisation
- [x] Make figures
- [x] Calculate node map positions
- [x] Make tests
- [ ] Clean up and divide large classes / encapsulation
- [ ] Write concious design choices
- [ ] Make lil video
- [ ] Make more tests

# potential additions: 
- Change data set interactively 
- Add more output message info after runnning ALNS
- Make map background cooler
- Add 'random' solver button
- Make dummy animation manager - split into interface

# concious design/programming choices
- output message in legend panel
- routemanager
- animationmanager object for each vehicle, so they operate individually
- runclicklistener with mapPanel in constructor 
- generic touple class 
- unmodifiable list in Nodemanager
- dummy RouteRenderer subclass 



Benefits of Refactoring
Improved Readability: Smaller, focused classes and methods are easier to understand.
Reusability: Modular classes can be reused in other parts of the application.
Testability: Smaller classes and methods are easier to test independently.
Maintainability: Changes in one part of the code are less likely to affect others.
