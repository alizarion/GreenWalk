//Typical Entities definition
//{type:"block", name:"wood", x:520,y:375,angle:90},
//{type:"villain", name:"burger",x:520,y:200,calories:590},
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

var box2d= {
    scale: 30,
    debug: false,
    init: function(){
        var gravity = new b2Vec2(0,9.8);    //declaration de la gravité 9.8 m/s^2 d'acceleration
        var allowSleep = true;//Permettre aux objets qui sont au repos à s'endormir et d'être exclus des calculs
        box2d.world = new b2World(gravity,allowSleep);
        box2d.world.IsLocked = function() { return false; }
        if(box2d.debug){
            debugContext = document.getElementById('debugcanvas').getContext('2d');
            var debugDraw  = new b2DebugDraw();
            debugDraw.SetSprite(debugContext);
            debugDraw.SetDrawScale(box2d.scale) ;
            debugDraw.SetFillAlpha(0.3);
            debugDraw.SetLineThickness(1.0);
            debugDraw.SetFlags(b2DebugDraw.e_shapeBit | b2DebugDraw.e_jointBit);
            // Start using debug draw in our world

            box2d.world.SetDebugDraw(debugDraw);
        }
        box2d.world.mouseInteraction = true;
        var listener = new Box2D.Dynamics.b2ContactListener;
        listener.PostSolve = function(contact,impulse){
            var body1  = contact.GetFixtureA().GetBody();
            var body2 = contact.GetFixtureB().GetBody();
            var entity1 = body1.GetUserData();
            var entity2 = body2.GetUserData();


            var impulseAlongNormal = Math.round(impulse.normalImpulses[0]);

            //ce listener est appeler trop frequement, il convient d'en prendre compte que sur les chocs violent

            if(impulseAlongNormal >5 ){
                if(entity1.health){
                    entity1.health -=impulseAlongNormal;
                }
                if(entity2.health){
                    entity2.health -=impulseAlongNormal;
                }
            }
        }

        box2d.world.SetContactListener(listener);
    },
    createRectangle: function(entity,definition){
        var bodyDef = new b2BodyDef;
        if(entity.isStatic){
            bodyDef.type = b2Body.b2_staticBody;
        } else {
            bodyDef.type = b2Body.b2_dynamicBody;
        }
        bodyDef.position.x = entity.x/box2d.scale;
        bodyDef.position.y = entity.y/box2d.scale;
        if(entity.angle){
            bodyDef.angle = Math.PI*entity.angle/180
        }
        var fixtureDef = new b2FixtureDef;
        fixtureDef.density = definition.density;
        fixtureDef.friction = definition.friction;
        fixtureDef.restitution = definition.restitution;

        fixtureDef.shape = new b2PolygonShape;
        fixtureDef.shape.SetAsBox(entity.width/2/box2d.scale,entity.height/2/box2d.scale);

        var body = box2d.world.CreateBody(bodyDef);
        body.SetUserData(entity);
        var fixture = body.CreateFixture(fixtureDef);
        return body;

    },
    createCircle : function(entity,definition){
        var bodyDef = new b2BodyDef;
        if(entity.isStatic){
            bodyDef.type = b2Body.b2_staticBody;
        } else {
            bodyDef.type = b2Body.b2_dynamicBody;
        }

        if(entity.angle){
            bodyDef.angle = Math.PI*entity.angle/180;
        }
        bodyDef.position.x  = entity.x/box2d.scale;
        bodyDef.position.y  = entity.y/box2d.scale;


        var fixtureDef = new b2FixtureDef;
        fixtureDef.density = definition.density;
        fixtureDef.restitution = definition.restitution;
        fixtureDef.friction = definition.friction;
        fixtureDef.shape = new b2CircleShape(entity.radius/box2d.scale);
        var body  = box2d.world.CreateBody(bodyDef);
        body.SetUserData(entity);
        var fixture = body.CreateFixture(fixtureDef);
        return body;

    } ,
    step:function(timeStep){
        // velocity iterations = 8
// position iterations = 3
        if(timeStep>2/60){
            timeStep = 2/60;
        }
        box2d.world.Step(timeStep,8,3);
    }
}