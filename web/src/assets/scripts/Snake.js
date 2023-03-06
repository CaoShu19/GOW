import { GmaeObject } from "./GameObject";
import { Cell } from "./Cell";

export class Snake extends GmaeObject{
    constructor(info,gamemap){
        super();

        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;

        //构建蛇，默认有个蛇头
        this.cells = [new Cell(info.r,info.c)];
        //蛇每秒走5个格子
        this.speed = 5; 
        //-1, 表示没有指令， 0 1 2 3 分别表是上下左右方向
        this.direction = -1;
        //idle 表示静止，move 表示移动，die表示死亡
        this.status = "idle";

        //下一步的目标位置
        this.next_cell = null;

        //定义四个方向偏移量
        this.dr = [-1,0,1,0];//行
        this.dc = [0,1,0,-1];//列

        //回合数目
        this.step = 0;
        //判定误差
        this.eps = 1e-2;

        //眼睛的方向:默认
        this.eye_direction = 0;
        if(this.id === 1) this.eye_direction = 2;

        this.eye_dx = [
            [-1,1],
            [1,1],
            [-1,1],
            [-1,-1],
        ];
        this.eye_dy = [
            [-1,-1],
            [-1,1],
            [1,1],
            [-1,1],
        ];
    }

    start(){

    }

    //检查当前回合蛇的长度是否增加
    check_tail_increasing(){
        if(this.step <= 10) return true;
        if(this.step % 3 === 1) return true;
        return false;
    }

    //用来设置方向
    set_direction(d){
        this.direction = d;
    }

    next_step(){
        //获取方向方向
        const d = this.direction;
        this.next_cell = new Cell(this.cells[0].r + this.dr[d],this.cells[0].c + this.dc[d]);
        this.eye_direction = d;
        //重置方向和状态,以及增加会和数
        this.direction = -1;
        this.status = "move";
        this.step ++;

        //把每个球都向后移动一位
        const k = this.cells.length;
        for(let i = k ; i > 0; i--){
            //需要深复制
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i-1]));
        }
        //如果下一个操作不合法（会撞墙或者撞蛇）
        if(!this.gamemap.check_valid(this.next_cell)){
            this.status = "die";
        }
    }

    

    //更新每一回合，将蛇的状态更新，并且进行渲染
    update_move(){
        
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx*dx + dy*dy);

        
        if(distance < this.eps){//移动到目标点
            this.cells[0] = this.next_cell;//将目标点作为真实头部
            this.next_cell = null;
            this.status = "idle";
            
            if(!this.check_tail_increasing()) this.cells.pop();
        }else{//没移动到目标点
            const move_distance = this.speed * this.timedelta / 1000;//每两帧之间
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;

            //若是不需要增加
            if(!this.check_tail_increasing()){
                const k = this.cells.length;

                const tail = this.cells[k-1];
                const tail_target = this.cells[k-2];

                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }
        }
    }   

    update(){
        if(this.status === "move"){
            this.update_move();
        }
        this.render();
    }
    //渲染一条蛇
    render(){
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle  = this.color;
        if(this.status  === "die"){
            ctx.fillStyle = "gray";
        }
        
        for(const cell of this.cells){
            //画一个圆
            ctx.beginPath();
            //坐标(x,y)，半径(L/2 * 0.8)，角度(0-2pi)
            ctx.arc(cell.x * L , cell.y * L, L/2*0.8,0, Math.PI*2);
            ctx.fill();
        }
        
        for(let i = 1 ; i < this.cells.length; i++){
            //遍历两个相邻的圆
            const a = this.cells[i-1], b = this.cells[i];
            if(Math.abs(a.x - b.x) < this.eps && Math.abs(a.y-b.y) < this.eps)
                continue;
            if(Math.abs(a.x - b.x) < this.eps){
                ctx.fillRect((a.x - 0.5 + 0.1)*L, Math.min(a.y,b.y)*L ,L*0.8, Math.abs(a.y - b.y)*L);
            }else{
                ctx.fillRect(Math.min(a.x,b.x)*L, (a.y - 0.5 + 0.1)*L ,Math.abs(a.x - b.x)*L, L*0.8);
            }
        }
        
        ctx.fillStyle = "black";
        for(let i = 0; i < 2; i++){
            const eye_x = (this.cells[0].x + this.eye_dx[this.eye_direction][i]*0.2)*L;
            const eye_y = (this.cells[0].y + this.eye_dy[this.eye_direction][i]*0.2)*L;
            ctx.beginPath();
            ctx.arc(eye_x,eye_y,L*0.05,0,Math.PI*2);
            ctx.fill()
        }
    }
}