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
// Start initializing objects, preloading assets and display start screen
    entities:[{type:"ground", name:"wood", x:500,y:400,width:10000,height:20,isStatic:true},
        {type:"ground", name:"wood", x:350,y:-1000,width:40,height:10000,isStatic:true},
        {type:"ground", name:"wood", x:20,y:-1000,width:40,height:10000,isStatic:true},
        {type:"waste",value:323, name:"menager-plastic",x:220,y:-1300,calories:590},
        {type:"waste",value:323, name:"electronic",x:220,y:-1200,calories:590},
        {type:"waste",value:323, name:"tin",x:220,y:-1100,calories:590},
        {type:"waste",value:323, name:"paper",x:220,y:-1000,calories:590},
        {type:"waste",value:323, name:"other",x:220,y:-900,calories:590},
        {type:"waste",value:323, name:"packaging-hazardous-materials",x:220,y:-800,calories:590},
        {type:"waste",value:323, name:"plastic-bag",x:220,y:-700,calories:590},
        {type:"waste",value:323, name:"wood-composite",x:220,y:-600,calories:590},
        {type:"waste",value:323, name:"textile",x:220,y:-500,calories:590},
        {type:"waste",value:323, name:"bottle-plastic",x:220,y:-100,calories:590},
        {type:"waste",value:323, name:"food-cardboard", x:280,y:0,calories:420}],
    init: function(){

        loader.init();
        garbage.canvas = $('#garbageCanvas')[0];
        garbage.context = garbage.canvas.getContext('2d');
        //console.log(garbage.context);
        console.log('fgdgsd');
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
            timeStep = (currentTime - garbage.lastUpdatedTime) /100;
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
    drawAllBodies:function(){

      box2d.world.DrawDebugData();
        //intérer sur les corp et les dessiner
        var awakeBodiesCount = 0 ;
        for(var body = box2d.world.GetBodyList();body; body = body.GetNext()){
            if(!body.IsAwake()){
                awakeBodiesCount++;
            }
            var entity = body.GetUserData();

            if(entity){
                var entityX = body.GetPosition().x*box2d.scale;
                entities.draw(entity,body.GetPosition(),body.GetAngle());
            }
        }
        if(awakeBodiesCount >= box2d.world.GetBodyCount()-1){
            garbage.sleeping = true;
        }
    }
}

$(window).load(function() {
    garbage.init();
});