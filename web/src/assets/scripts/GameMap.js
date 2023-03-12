import { GmaeObject } from "./GameObject";
import { Snake } from "./Snake";
import { Wall } from "./Wall";


export class GameMap extends GmaeObject{
    //传入画布和画布的父元素
    constructor(ctx,parent,store){
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.store = store;
        this.L = 0;


        this.rows = 13;
        this.cols = 14;


        this.inner_walls_count = 30;
        this.walls = [];


        //在地图中创建蛇
        this.snakes = [
            new Snake({id:0,color:"#4876EC", r:this.rows - 2, c:1},this),
            new Snake({id:0,color:"#F94848", r:1, c:this.cols - 2},this),
        ];

        //眼睛的方向
        this.eye_direction = 0;//0朝上 2 朝下
        if(this.id === 1) this.eye_direction = 2;
    }


    // //DFS:判断两个坐标之间是否联通
    // check_connectivity(g,sx,sy,tx,ty){
    //     if(sx == tx && sy == ty) return true;
    //     g[sx][sy] = true;
        
    //     let dx = [-1,0,1,0],dy = [0,1,0,-1];
    //     //遍历4个方向
    //     for(let i = 0; i < 4 ;i ++){
    //         let x = sx + dx[i], y = sy + dy[i];
    //         if(!g[x][y] && this.check_connectivity(g,x,y,tx,ty)) return true;
    //     }
    //     return false;
    // }

    // create_walls(){ 
    //     const g = [];
    //     for(let r = 0; r < this.rows; r++){
    //         g[r] = [];
    //         for(let c = 0 ; c < this.cols; c++){
    //             g[r][c] = false;
    //         }
    //     }
    //     //创建四周障碍物
    //     for(let r = 0; r < this.rows ; r ++){
    //         g[r][0] = g[r][this.cols - 1]  = true;
    //     }
    //     for(let c = 0 ; c < this.cols ; c++){
    //         g[0][c] = g[this.rows-1][c] = true;
    //     }


    //     //创建随机障碍物
    //     for(let i = 0; i < this.inner_walls_count/2; i++){
    //         for(let j = 0; j < 1000; j++){
    //             let r = parseInt(Math.random()*this.rows);
    //             let c = parseInt(Math.random()*this.cols);

    //             if(g[r][c] || g[this.rows - 1 - r][this.cols - 1 - c]) continue;
    //             if(r == this.rows -2 && c == 1 || r == 1 && c == this.cols -2) continue;

    //             g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = true;
    //             break;
    //         }
    //     }
    //     const copy_g = JSON.parse(JSON.stringify(g));
    //     if(!this.check_connectivity(copy_g,this.rows-2,1,1,this.cols-2)) return false;

    //     //真实填充canvas
    //     for(let r = 0; r < this.rows ; r++){
    //         for(let c = 0; c < this.cols; c++){
    //             if(g[r][c]){
    //                 this.walls.push(new Wall(r,c,this));
    //             }
    //         }
    //     }

    //     return true;
    // }

     
     create_walls(){ //创建地图墙
        const g = this.store.state.pk.gamemap;
    
        //真实填充canvas
        for(let r = 0; r < this.rows ; r++){
            for(let c = 0; c < this.cols; c++){
                if(g[r][c]){
                    this.walls.push(new Wall(r,c,this));
                }
            }
        }
        console.log(g);
        return true;
    }


    check_ready(){
        //当两条蛇都准备好（状态是idle和没有指令）后就可以移动
        for(const snake of this.snakes){
            if(snake.status != "idle") return false;
            if(snake.direction === -1) return false;
        }
        return true;
    }

    add_lilstening_events(){
        if(this.store.state.record.is_record){
            console.log("进入回放");
            let k = 0;//第几回合
            const a_steps = this.store.state.record.a_steps;
            const b_steps = this.store.state.record.b_steps;
            const loser = this.store.state.record.record_loser;
            const [snake0,snake1] = this.snakes;
            const interval_id = setInterval(() =>{
                if(k >= a_steps.length - 1){
                    if(loser === "all" || loser === "A"){
                        snake0.status = "die";
                    }
                    if(loser === "all" || loser === "B"){
                        snake1.status = "die";
                    }
                    clearInterval(interval_id);
                }else{
                    snake0.set_direction(parseInt(a_steps[k]));
                    snake1.set_direction(parseInt(b_steps[k]));
                }
                k++;
            },300)
        }else{
            this.ctx.canvas.focus();

            this.ctx.canvas.addEventListener("keydown",e=>{
                let d = -1;
                if(e.key === 'w')d = 0;
                else if(e.key === 'd') d = 1;
                else if(e.key === 's') d = 2;
                else if(e.key === 'a') d = 3;
    
                if(d >= 0){
                    this.store.state.pk.socket.send(JSON.stringify({
                        event:"move",
                        direction:d,
                    }))
                }
            })
        }
    }

    start(){
        console.log("创建地图")
        this.create_walls();

        //执行监听函数
        this.add_lilstening_events();
    }

    update_size(){
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));

        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;

    }

    //让两条蛇都进入下一回合
    next_step(){
        for(const snake of this.snakes){
            snake.next_step();
        }
    }

    //检测目标位置是否合法：撞墙和蛇身体
    check_valid(cell){
        for(const wall of this.walls){
            if(wall.r === cell.r && wall.c === cell.c){
                return false;
            }
        }

        for(const snake of this.snakes){
            let k = snake.cells.length;
            if(!snake.check_tail_increasing()){ //当蛇尾部不增加的时候，蛇尾不判断
                k--;
            }
            for(let i = 0; i < k ; i++){
                if(snake.cells[i].r === cell.r && snake.cells[i].c === cell.c)
                return false;
            }
        }
        return true;
    }

    update(){
        this.update_size();
        if(this.check_ready()){
            this.next_step();
        }
        this.render();
    }

    render(){
        const color_even = "#AAD751",color_odd = "#FFFFFF"
        for(let r = 0 ; r < this.rows; r++){
            for(let c = 0; c < this.cols; c++){
                if((r+c)%2 == 0){
                    this.ctx.fillStyle = color_even;
                }else{
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(c*this.L,r*this.L,this.L,this.L);
            }
        }

    }
}