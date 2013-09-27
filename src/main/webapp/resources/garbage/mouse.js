var mouse = {
    x:0,
    y:0,
    down:false,
    init:function(){
        $("#garbageCanvas").mousemove(mouse.mousemovehandler);
        $('#garbageCanvas').mousedown(mouse.mousedownhandler);
        $('#garbageCanvas').mouseup(mouse.mouseuphandler);
        $('#garbageCanvas').mouseout(mouse.mouseuphandler);

       // document.querySelector('#gamecanvas').addEventListener('pointerdown', mouse.mousedownhandler,false) ;
        //document.querySelector('#gamecanvas').addEventListener('pointermove', mouse.mousemovehandler,false) ;
        //document.querySelector('#gamecanvas').addEventListener('pointerup', mouse.mouseuphandler,false) ;
        //document.querySelector('#gamecanvas').addEventListener('pointerout', mouse.mouseuphandler,false) ;
    },
    mousemovehandler:function(ev){
        var offset = $('#garbageCanvas').offset();
        mouse.x = ev.pageX - offset.left;
        mouse.y = ev.pageY - offset.top;
        //console.log(ev.pageX,ev.pageY);
        if(mouse.down){
            mouse.dragging = true;
           // console.log('mouse x = '+mouse.x + ', mouse y = '+ mouse.y+' dragging = ' + mouse.dragging );
        }

    },
    mousedownhandler:function(ev){
        mouse.down = true;
        mouse.downX = mouse.x;
        mouse.downY = mouse.y;
        console.log(ev.pageX,ev.pageY);
        //on annule l'action provoqué par l'evenement par defaut
        ev.originalEvent.preventDefault();
    },
    mouseuphandler: function(ev){
        mouse.down = false ;
        mouse.dragging = false;
     //   console.log(ev.pageX,ev.pageY);
    }

}