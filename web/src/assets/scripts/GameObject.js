const GAME_OBJECT = [];

//游戏对象基类
export class GmaeObject{
    constructor(){
        GAME_OBJECT.push(this);

        //时间间隔：我们希望知道游戏对象的移动速度，由于游戏运动是靠每帧画出来不同的像素执行起来的，
        //所以每帧该不该画，涉及到时间
        //游戏对象两帧之间的时间间隔
        this.timedelta = 0;

        //当前游戏对象是否被调用过start和update一次
        this.has_called_start  = false;

    }


    start(){

    }

    update(){

    }

    on_destory(){//删除前的回调函数

    }

    destroy() {
        this.on_destory();

        for(let i in GAME_OBJECT){
             const obj = GAME_OBJECT[i];
            if(obj === this){
                GAME_OBJECT.splice(i);
                break;
            }
        }
    }

}

let last_timestamp;//上一次执行的时间

const step= timestamp =>{
    for(let obj of GAME_OBJECT){
        if(!obj.has_called_start){
            obj.has_called_start = true;
            obj.start();
        }else{
            obj.timedelta = timestamp - last_timestamp;
            obj.update();
        }
    }
    last_timestamp = timestamp;
    requestAnimationFrame(step)
}

//让游戏对象每帧都重新渲染一边

//在浏览器再某一帧时会调用此API，可以传入一个回调函数让其执行
//可以通过递归的方式实现每一帧执行
requestAnimationFrame(step)