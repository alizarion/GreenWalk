var entities = {
    definitions : {
        "food-cardboard":{
            shape: "circle",
            radius :40,
            density:1,
            friction :0.5,
            restitution :0.4
        },
        "electronic":{
            shape:"circle",
            radius:40,
            density:2.0,
            friction:0.5,
            restitution:0.4
        },
        "tin":{
            shape:"circle",
            radius:40,
            density:2.0,
            friction:0.5,
            restitution:0.4
        },
        "paper":{
            shape:"circle",
            radius:40,
            density:2.0,
            friction:0.5,
            restitution:0.4
        },
        "menager-plastic":{
            shape:"circle",
            radius:40,
            density:2.0,
            friction:0.5,
            restitution:0.4
        } ,
        "bottle-plastic":{
            shape:"circle",
            radius:40,
            density:2.0,
            friction:0.5,
            restitution:0.4
        } ,
        "bottle-glass":{
            shape:"circle",
            radius:40,
            density:2.0,
            friction:0.5,
            restitution:0.4
        },
        "jars":{
            shape:"circle",
            radius:40,
            density:2.0,
            friction:0.5,
            restitution:0.4
        },
        "other":{
            shape:"circle",
            radius:40,
            density:2.0,
            friction:0.5,
            restitution:0.4
        } ,
        "packaging-hazardous-materials":{
            shape:"circle",
            radius:40,
            density:2.0,
            friction:0.5,
            restitution:0.4
        } ,
        "plastic-bag":{
            shape:"circle",
            radius:40,
            density:2.0,
            friction:0.5,
            restitution:0.4
        } ,
        "textile":{
            shape:"circle",
            radius:40,
            density:2.0,
            friction:0.5,
            restitution:0.4
        },
        "wood-composite":{
            shape:"circle",
            radius:40,
            density:2.0,
            friction:0.5,
            restitution:0.4
        },

        "wood":{
            fullHealth:500,
            density: 0.7,
            friction : 0.4,
            restitution :  0.4
        },
        "dirt":{
            density:3.0,
            friction : 0.4,
            restitution :  0.15
        }

    },
    create : function(entity){
        var definition = entities.definitions[entity.name];
        if(!definition){
            // console.log('entitée ',entity.name,' non reconnu');
            return;
        }
        switch (entity.type){
            case "block": // simple block
                entity.shape = "rectangle";
                entity.sprite = loader.loadImage('../resources/img/garbage/pico_'+entity.name+".png");
                box2d.createRectangle(entity,definition);
                //   console.log('creation d une entité de type :  ', entity.type);
                break;
            case "ground" : // simple rectangle
                //pas besoin de vie ces blocks sont indestructible
                entity.shape = "rectangle";
                definition.friction = 7;
                box2d.createRectangle(entity,definition);
                //  console.log('creation d une entité de type :  ', entity.type);

                break;
            case "hero" : // simple cercle
                //pas besoin de vie ils sont pour l'instant indestructible
                entity.shape = "circle"
                entity.radius = definition.radius;
                entity.fired = false;
                entity.sprite = loader.loadImage('../resources/img/garbage/picto_'+entity.name+".png");
                box2d.createCircle(entity,definition);
                //   console.log('creation d une entité de type :  ', entity.type);
                break;
            case "waste" : //peut etre cercle ou rectangle
                entity.shape = definition.shape;
                entity.sprite = loader.loadImage('../resources/img/garbage/picto_'+entity.name+".png");
                if(definition.shape == 'circle'){
                    entity.radius = definition.radius;
                    box2d.createCircle(entity,definition);
                } else {
                    entity.width = definition.width;
                    entity.height = definition.height;
                    box2d.createRectangle(entity,definition);
                }
                //   console.log('creation d une entité de type :  ', entity.type);
                break;
            default :
                // console.log("type inféfinie pour ", entity.type);
                break;
        }
    },
    //dessiner dans le canvas garbage les entité en fonction de leurs position et angle
    draw: function(entity,position,angle){
        // console.log('draw');
        garbage.context.translate(position.x*box2d.scale-garbage.offsetLeft,position.y*box2d.scale);
        garbage.context.rotate(angle);


        switch (entity.type){
            case "block":
                garbage.context.drawImage(entity.sprite,0,0,entity.sprite.width,entity.sprite.height,
                    -entity.width/2-1,-entity.height/2-1,entity.width+2,entity.height+2);

                break;
            case "waste":
                if (entity.shape=="circle"){
                    garbage.context.drawImage(entity.sprite,0,0,entity.sprite.width,entity.sprite.height,
                        -entity.radius-1,-entity.radius-1,entity.radius*2+2,entity.radius*2+2);
                    garbage.context.font = "17pt Boogaloo regular";
                    garbage.context.strokeStyle = "#00323f";
                    garbage.context.textAlign  = 'center';
                    garbage.context.fillText(entity.value,0,25);
                } else if (entity.shape=="rectangle"){
                    garbage.context.drawImage(entity.sprite,0,0,entity.sprite.width,entity.sprite.height,
                        -entity.width/2-1,-entity.height/2-1,entity.width+2,entity.height+2);
                    garbage.context.font = "17pt Boogaloo regular";
                    garbage.context.strokeStyle = "#00323f";
                    garbage.context.textAlign  = 'center';
                    garbage.context.fillText(entity.value,0,25);
                }
                break;
            case "hero":
                if (entity.shape=="circle"){
                    garbage.context.drawImage(entity.sprite,0,0,entity.sprite.width,entity.sprite.height,
                        -entity.radius-1,-entity.radius-1,entity.radius*2+2,entity.radius*2+2);
                } else if (entity.shape=="rectangle"){
                    garbage.context.drawImage(entity.sprite,0,0,entity.sprite.width,entity.sprite.height,
                        -entity.width/2-1,-entity.height/2-1,entity.width+2,entity.height+2);
                }
                break;
            case "ground":
// do nothing... We will draw objects like the ground & slingshot separately
                break;
        }
        garbage.context.rotate(-angle);
        garbage.context.translate(-position.x*box2d.scale+garbage.offsetLeft,-position.y*box2d.scale);

    }
}