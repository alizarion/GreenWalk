var loader={
    loaded : true,
    loadedCount:0,
    totalCount:0,

    init:function(){
        var mp3Support,oggSupport;
        var audio = document.createElement('audio');

        if(audio.canPlayType){
            mp3Support = '' != audio.canPlayType('audio/mpeg');
            oggSupport = '' != audio.canPlayType("audio/ogg; codecs='vorbis'");
        } else {
            mp3Support = false;
            oggSupport = false;
        }

        loader.soundFileExtn = mp3Support ? '.mp3' : oggSupport ?'ogg':undefined;
    },
    loadImage:function(url){
        this.totalCount++;
        this.loaded = false;
        $('#loadingscreen').show();

        var  image = new Image();
        image.src = url;
        console.log(url) ;
        image.onload=loader.itemLoaded;
        return image;
    },
    soundFileExtn:".mp3",
    loadSound:function(url){
        this.totalCount++;
        this.loaded = false;
        $('#loadingscreen').show();
        var audio = new Audio();
        audio.src = url + loader.soundFileExtn;
        audio.addEventListener("canplaythrough", loader.itemLoaded,false);
        return audio;
    },
    itemLoaded:function(){

        loader.loadedCount++;
     //   console.log(loader.loadedCount);
        $('#loadingmessage').html('Loaded'+loader.loadedCount+' of '+ loader.totalCount);
    //    console.log('hey') ;
        if(loader.loadedCount == loader.totalCount){
            //loader has loaded completely
            loader.loaded = true;
            // cacher la barre de lancement
            $('#loadingscreen').hide('slow');
            if(loader.onload){
                loader.onload();
                loader.onload = undefined;
            }
        }
    }

}