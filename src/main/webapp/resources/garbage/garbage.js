var timeStep = 1/60;
var velocityIterations = 8;
var positionIterations = 3;

(function() {
    var lastTime = 0;
    var vendors = ['ms', 'moz', 'webkit', 'o'];
    for(var x = 0; x < vendors.length && !window.requestAnimationFrame; ++x) {
        window.requestAnimationFrame = window[vendors[x] + 'RequestAnimationFrame'];
        window.cancelAnimationFrame =
            window[vendors[x] + 'CancelAnimationFrame'] ||
                window[vendors[x] + 'CancelRequestAnimationFrame'];
    }
    if (!window.requestAnimationFrame)
        window.requestAnimationFrame = function(callback, element) {
            var currTime = new Date().getTime();
            var timeToCall = Math.max(0, 16 - (currTime - lastTime));
            var id = window.setTimeout(function() { callback(currTime + timeToCall); },
                timeToCall);
            lastTime = currTime + timeToCall;
            return id;
        };
    if (!window.cancelAnimationFrame)
        window.cancelAnimationFrame = function(id) {
            clearTimeout(id);
        };
}());

var garbage = {
    width : 557,
    height : 430,
// Start initializing objects, preloading assets and display start screen
    entities: [{type:"ground", name:"wood", x:500,y:400,width:10000,height:20,isStatic:true},
        {type:"ground", name:"wood", x:350,y:-1000,width:40,height:10000,isStatic:true},
        {type:"ground", name:"wood", x:20,y:-1000,width:40,height:10000,isStatic:true}]
    ,
    init: function(){


        garbage.canvas = $('#garbageCanvas')[0];
        garbage.context = garbage.canvas.getContext('2d');
        garbage.context.scale(garbage.width/557, garbage.height/430);
        garbage.context.drawImage(garbage.background,0,0);

        $('#garbageCanvas').show();
        loader.init();
        //console.log(garbage.context);
        mouse.init();
        box2d.init();
        for (var i = garbage.entities.length-1; i >= 0; i--){
            var entity = garbage.entities[i];
            entities.create(entity);
        };
        garbage.start();
    } ,
    background:loader.loadImage("../resources/img/garbage/garbage_home.png")
    ,
    start :function(){
        $('.garbagelayer').hide();

        $('#garbageCanvas').show();
        garbage.offsetLeft=0;
        garbage.animationFrame = window.requestAnimationFrame(garbage.animate,garbage.canvas);
    },

    animate:function() {

        var currentTime = new Date().getTime();
        var timeStep;
        if(garbage.lastUpdatedTime){
            timeStep = (currentTime - garbage.lastUpdatedTime) /60;
            box2d.step(timeStep);
        }
        garbage.lastUpdatedTime = currentTime;
        //   garbage.canvas.width= garbage.canvas.width;
        garbage.context.clearRect(0, 0, garbage.canvas.width, garbage.canvas.height);
        garbage.context.drawImage(garbage.background,0,0);
        // Draw all the bodies
        garbage.drawAllBodies();
        if(!garbage.sleeping){
            garbage.animationFrame = window.requestAnimationFrame(garbage.animate,garbage.canvas);
        } else{
            console.log('garbage is sleeping');
        }

    },
    sleeping: false,
    pushEntities:function(newEntity){
        for(var body = box2d.world.GetBodyList();body; body = body.GetNext()){
            var entity = body.GetUserData();
            if(entity){
                if(entity.name == newEntity.name){
                    box2d.world.DestroyBody(body);
                }
            }
        }
        garbage.entities.push(newEntity);
        entities.create(newEntity);
        garbage.sleeping=false;
        garbage.animate();
    },
    drawAllBodies:function(){

        box2d.world.DrawDebugData();
        //intérer sur les corp et les dessiner
        var awakeBodiesCount = 0 ;
        var bodyCount = 0 ;
        for(var body = box2d.world.GetBodyList();body; body = body.GetNext()){
            var entity = body.GetUserData();
            if(entity){
                if(entity.type=='waste'){
                    bodyCount++;
                    if(!body.IsAwake()){
                        awakeBodiesCount++;
                    }
                }
                entities.draw(entity,body.GetPosition(),body.GetAngle());

            }
        }

        if(awakeBodiesCount >=bodyCount){
            garbage.sleeping = true;
        }
    }

}
