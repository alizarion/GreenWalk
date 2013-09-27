var b2Vec2 = Box2D.Common.Math.b2Vec2;
var b2BodyDef = Box2D.Dynamics.b2BodyDef;
var b2Body = Box2D.Dynamics.b2Body;
var b2FixtureDef = Box2D.Dynamics.b2FixtureDef;
var b2Fixture = Box2D.Dynamics.b2Fixture;
var b2World = Box2D.Dynamics.b2World;
var b2PolygonShape = Box2D.Collision.Shapes.b2PolygonShape;
var b2CircleShape = Box2D.Collision.Shapes.b2CircleShape;
var b2DebugDraw = Box2D.Dynamics.b2DebugDraw;
var b2RevoluteJointDef = Box2D.Dynamics.Joints.b2RevoluteJointDef;


var world;

var scale = 30;  //pixels dans le canvas correspond à 1 metre dans la réalitée
var context;
var timeStep = 1/60;
var velocityIterations = 8;
var positionIterations = 3;


var mouseX, mouseY, mousePVec, isMouseDown, selectedBody, mouseJoint;



function init(){

    //Parametrage du box2d world qui permettra la plupart des calcules physiques
    var gravity = new b2Vec2(0,9.8);    //declaration de la gravité 9.8 m/s^2 d'acceleration
    var allowSleep = true;//Permettre aux objets qui sont au repos à s'endormir et d'être exclus des calculs
    world = new b2World(gravity,allowSleep);
    world.IsLocked = function() { return false; };

    createFloor();
    createSpecialBody();
    createComplexBody();
    createRectangularBody();
    createCircularBody2();
    createRevoluteJoint() ;
    listenForContact();
    /*

     createSimplePolygonBody();
     createCircularBody();

     */

    setupDebugDraw();
    mouse.init();
//    world.DrawDebugData();
    animate();

}

var mouse = {
    x:0,
    y:0,
    down:false,
    init:function(){
        $("#canvas").mousemove(mouse.mousemovehandler);
        $('#canvas').mousedown(mouse.mousedownhandler);
        $('#canvas').mouseup(mouse.mouseuphandler);
        $('#canvas').mouseout(mouse.mouseuphandler);
    },
    mousemovehandler:function(ev){
        var offset = $('#canvas').offset();
        mouse.x = ev.pageX - offset.left;
        mouse.y = ev.pageY - offset.top;
        if(mouse.down){
            mouse.dragging = true;
            // console.log('mouse x = '+mouse.x + ', mouse y = '+ mouse.y+' dragging = ' + mouse.dragging );
        }

    },
    mousedownhandler:function(ev){
        mouse.down = true;
        mouse.downX = mouse.x;
        mouse.downY = mouse.y;
        //on annule l'action provoqué par l'evenement par defaut
        ev.originalEvent.preventDefault();
    },
    mouseuphandler: function(ev){
        mouse.down = false ;
        mouse.dragging = false;
    }

}


function createRectangularBody(){
    var bodyDef = new b2BodyDef;
    bodyDef.type = b2Body.b2_dynamicBody;
    bodyDef.position.x = 40/scale;
    bodyDef.position.y = 100/scale;
    var fixtureDef = new b2FixtureDef;
    fixtureDef.density = 1.0;
    fixtureDef.friction = 0.5;
    fixtureDef.restitution = 0.3;
    fixtureDef.shape = new b2PolygonShape;
    fixtureDef.shape.SetAsBox(30/scale,50/scale);
    var body = world.CreateBody(bodyDef);
    var fixture = body.CreateFixture(fixtureDef);
}


function getBodyAtMouse() {

    mousePVec = new b2Vec2(mouse.x, mouse.y);
    var aabb = new b2AABB();
    aabb.lowerBound.Set(mouse.x - 0.001, mouse.y - 0.001);
    aabb.upperBound.Set(mouse.x + 0.001,mouse.y + 0.001);

    // Query the world for overlapping shapes.

    selectedBody = null;

    world.QueryAABB(getBodyCB, aabb);
    return selectedBody;
}

function getBodyCB(fixture) {
    console.log(fixture.GetBody().GetType()) ;
    if(fixture.GetBody().GetType() != b2Body.b2_staticBody) {
        if(fixture.GetShape().TestPoint(fixture.GetBody().GetTransform(), mousePVec)) {
            selectedBody = fixture.GetBody();
            return false;
        }
    }
    return true;
}
var count = 0;
function animate(){
    world.Step(timeStep,velocityIterations,positionIterations);
    /*  if(count < 100 ){
     createSpecialBody();
     count++;
     }      */
    /*if(count <200 && count> 99){
     createSimplePolygonBody();
     count++;
     }
     if(count <300 && count> 199){
     createRectangularBody();
     count++;
     } */
    // Custom Drawing
    world.ClearForces();
    world.DrawDebugData();
    if(specialBody){
        drawSpecialBody();

        if(specialBody.GetUserData().life <=0){
            world.DestroyBody(specialBody);
            specialBody =undefined;
        }
    }




    setTimeout(animate,timeStep);
}

function createComplexBody(){
    var bodyDef = new b2BodyDef;
    bodyDef.type  = b2Body.b2_dynamicBody;
    bodyDef.position.x = 350/scale;
    bodyDef.position.y = 50/scale;
    var body = world.CreateBody(bodyDef);

    var  fixtureDef = new b2FixtureDef;
    fixtureDef.density = 1.0;
    fixtureDef.friction = 0.5;
    fixtureDef.restitution = 0.7;
    fixtureDef.shape = new b2CircleShape(40/scale);
    body.CreateFixture(fixtureDef);

    fixtureDef.shape = new b2PolygonShape;
    var points = [
        new b2Vec2(0,0),
        new b2Vec2(40/scale,50/scale),
        new b2Vec2(50/scale,100/scale),
        new b2Vec2(-50/scale,100/scale),
        new b2Vec2(-40/scale,50/scale)
    ];

    fixtureDef.shape.SetAsArray(points,points.length);
    body.CreateFixture(fixtureDef);
}

function createRevoluteJoint(){
    //premier corp
    var bodyDef1 = new b2BodyDef ;
    bodyDef1.type = b2Body.b2_dynamicBody;
    bodyDef1.position.x = 480/scale;
    bodyDef1.position.y = 50/scale;
    var body1 = world.CreateBody(bodyDef1);

    var fixtureDef1 = new b2FixtureDef;
    fixtureDef1.density = 1.0;
    fixtureDef1.restitution = 0.5;
    fixtureDef1.friction = 0.5;
    fixtureDef1.shape = new b2PolygonShape;
    fixtureDef1.shape.SetAsBox(50/scale,10/scale);
    body1.CreateFixture(fixtureDef1);

    var bodyDef2 = new b2BodyDef;
    bodyDef2.type = b2Body.b2_dynamicBody;
    bodyDef2.position.x = 470/scale;
    bodyDef2.position.y = 50/scale;
    var body2 = world.CreateBody(bodyDef2);

    var fixtureDef2 =  new b2FixtureDef;
    fixtureDef2.density = 1.0;
    fixtureDef2.friction = 0.5;
    fixtureDef2.restitution = 0.5;
    fixtureDef2.shape = new b2PolygonShape;
    var points = [
        new b2Vec2(0,0),
        new b2Vec2(40/scale,50/scale),
        new b2Vec2(50/scale,100/scale),
        new b2Vec2(-50/scale,100/scale),
        new b2Vec2(-40/scale,50/scale)
    ];
    fixtureDef2.shape.SetAsArray(points,points.length);
    body2.CreateFixture(fixtureDef2);

    var joinDef = new b2RevoluteJointDef;
    var jointCenter = new b2Vec2(470/scale,50/scale);
    joinDef.Initialize(body1,body2,jointCenter);
    world.CreateJoint(joinDef);

}


function createFloor(){

    //la définition du corp contient toute les donnée qui vont permettre de creer notre corp rigide
    var bodyDef = new b2BodyDef;
    bodyDef.type = b2Body.b2_staticBody;
    bodyDef.position.x = 640/2/scale;
    bodyDef.position.y = 450/scale;
    var body  = world.CreateBody(bodyDef);
    // fixture permet de decrire les proprietés physique d'un objet(taille,matériaux,forme),
    // chaque objet peut avoir plusieurs fixture ce qui auras un impacte sur son centre de gravité
    var fixtureDef = new b2FixtureDef;
    fixtureDef.density = 1.0;
    fixtureDef.friction = 0.5;
    fixtureDef.restitution = 0.3;   //rebond

    fixtureDef.shape = new b2PolygonShape;
    fixtureDef.shape.SetAsBox(640/scale,10/scale);

    body.CreateFixture(fixtureDef);
    var fixtureDef2 = new b2FixtureDef;
    fixtureDef2.density = 1.0;
    fixtureDef2.friction = 0.5;
    fixtureDef2.restitution = 0.3;   //rebond
    fixtureDef2.shape = new b2PolygonShape;
    var points = [
        new b2Vec2(250/scale,0/scale),
        new b2Vec2(300/scale,-400/scale),
        new b2Vec2(640/scale,450/scale)

    ];

    fixtureDef2.shape.SetAsArray(points,points.length);
    body.CreateFixture(fixtureDef2);


}
function createCircularBody(){
    var bodyDef = new b2BodyDef;
    bodyDef.type = b2Body.b2_dynamicBody;
    bodyDef.position.x = 60/scale;
    bodyDef.position.y = 5/scale;
    var fixtureDef = new b2FixtureDef;
    fixtureDef.density = 1.0;
    fixtureDef.friction = 0.5;
    fixtureDef.restitution = 0.7;
    fixtureDef.shape = new b2CircleShape(30/scale);
    var body = world.CreateBody(bodyDef);
    var fixture = body.CreateFixture(fixtureDef);

}
function createSimplePolygonBody(){
    var bodyDef = new b2BodyDef;
    bodyDef.type = b2Body.b2_dynamicBody;
    bodyDef.position.x = 230/scale;
    bodyDef.position.y = 50/scale;
    var fixtureDef = new b2FixtureDef;
    fixtureDef.density = 1.0;
    fixtureDef.friction = 0.5;
    fixtureDef.restitution = 0.2;
    fixtureDef.shape = new b2PolygonShape;
// Create an array of b2Vec2 points in clockwise direction
    var points = [
        new b2Vec2(0,0),
        new b2Vec2(40/scale,50/scale),
        new b2Vec2(50/scale,100/scale),
        new b2Vec2(-50/scale,100/scale),
        new b2Vec2(-40/scale,50/scale)
    ];
    //si les point sont défini dans le sens inverse des aiguilles d'une montre la gestion de collision de ne fonctionera pas
    /*  var points = [
     new b2Vec2(-40/scale,50/scale),
     new b2Vec2(-50/scale,100/scale),
     new b2Vec2(50/scale,100/scale),
     new b2Vec2(40/scale,50/scale),
     new b2Vec2(0,0)
     ];   */
// Use SetAsArray to define the shape using the points array
    fixtureDef.shape.SetAsArray(points,points.length);
    var body = world.CreateBody(bodyDef);
    var fixture = body.CreateFixture(fixtureDef);
}

function createCircularBody2(){
    var bodyDef = new b2BodyDef;
    bodyDef.type = b2Body.b2_dynamicBody;
    bodyDef.position.x = 100/scale;
    bodyDef.position.y = 5/scale;
    var fixtureDef = new b2FixtureDef;
    fixtureDef.density = 1.0;
    fixtureDef.friction = 0.5;
    fixtureDef.restitution = 0.7;
    fixtureDef.shape = new b2CircleShape(30/scale);
    var body = world.CreateBody(bodyDef);
    var fixture = body.CreateFixture(fixtureDef);

}


function setupDebugDraw(){
    context = document.getElementById('canvas').getContext('2d');
    var debugDraw  = new b2DebugDraw();
    debugDraw.SetSprite(context);
    debugDraw.SetDrawScale(scale) ;
    debugDraw.SetFillAlpha(0.3);
    debugDraw.SetLineThickness(1.0);
    debugDraw.SetFlags(b2DebugDraw.e_shapeBit | b2DebugDraw.e_jointBit);
    // Start using debug draw in our world
    world.mouseInteraction = true;
    world.SetDebugDraw(debugDraw);

}

var specialBody;
function createSpecialBody(){

    var bodyDef = new b2BodyDef;
    bodyDef.type = b2Body.b2_dynamicBody;
    bodyDef.position.x = 450/scale;
    bodyDef.position.y = 0/scale;
    specialBody = world.CreateBody(bodyDef);
    specialBody.SetUserData({name:"special",life:250});

    // ajout d'une fixture pour attacher  une forme circulaire au body
    var fixtureDef = new b2FixtureDef;
    fixtureDef.density = 1.0;
    fixtureDef.restitution = 0.5;
    fixtureDef.friction = 0.5;

    fixtureDef.shape = new b2CircleShape(30/scale);
    var fixture = specialBody.CreateFixture(fixtureDef);
}

function listenForContact(){
    var listener = new Box2D.Dynamics.b2ContactListener;
    listener.PostSolve = function(contact,impulse){
        var body1 = contact.GetFixtureA().GetBody();
        var body2 = contact.GetFixtureB().GetBody();
// If either of the bodies is the special body, reduce its life
        if (body1 == specialBody || body2 == specialBody){
            var impulseAlongNormal = impulse.normalImpulses[0];
            specialBody.GetUserData().life -= impulseAlongNormal;
            console.log("The special body was in a collision with impulse", impulseAlongNormal,"and" +
                "its life has now become ",specialBody.GetUserData().life);
        }
    };
    world.SetContactListener(listener);
}


function drawSpecialBody() {
    var position = specialBody.GetPosition();
    var angle =  specialBody.GetAngle();

    context.translate(position.x*scale,position.y*scale);
    context.rotate(angle);

    //dessiner un visage
    context.fillStyle = "rgb(200,150,255);";
    context.beginPath();
    context.arc(0,0,30,0,2*Math.PI,false);
    context.fill();

    //deux rectangles pour les yeux

    context.fillStyle = "rgb(255,255,255);";
    context.fillRect(-15,-15,10,5);
    context.fillRect(5,-15,10,5);



    //dessiner le sourir en fonction du niveau de vie
    context.strokeStyle="rgb(255,255,255);" ;
    context.beginPath();
    if(specialBody.GetUserData().life >100){
        context.arc(0,0,10,Math.PI,2*Math.PI,true);
    }   else{
        context.arc(0,10,10,Math.PI,2*Math.PI,false);
        context.fillRect(5,-10,3,6);
    }
    context.stroke();
    // Translate and rotate axis back to original position and angle
    context.rotate(-angle);
    context.translate(-position.x*scale,-position.y*scale);
}
