import { fromEvent,interval,Observable, Subscription } from 'rxjs'; 
import { map,filter,flatMap,takeUntil,scan,merge, buffer} from 'rxjs/operators';

type Key = 'ArrowLeft' | 'ArrowRight' | 'Space' | 'KeyE'
type Event = 'keydown' | 'keyup'

function spaceinvaders() {
    // Inside this function you will use the classes and functions 
    // from rx.js
    // to add visuals to the svg element in pong.html, animate them, and make them interactive.
    // Study and complete the tasks in observable exampels first to get ideas.
    // Course Notes showing Asteroids in FRP: https://tgdwyer.github.io/asteroids/ 
    // You will be marked on your functional programming style
    // as well as the functionality that you implement.
    // Document your code!  

  class Tick { constructor(public readonly elapsed:number) {} }
  class Move { constructor(public readonly direction:number) {} }
  class Shoot { constructor() {} }
  class Ult { constructor() {} }

  // Get HTLM elements and store them in variables
  const ship = document.getElementById("ship")!;
  const canvas = document.getElementById("canvas")!;
  // Create a score counter
  const score = document.createElementNS(canvas.namespaceURI, "text")!;
  attr(score,{
    x: 20,
    y: 20,
    fill: "white",
    class: "score"
  });
  score.textContent = "Score : 0";
  canvas.appendChild(score);
  // Create a high score counter
  const highscore = document.createElementNS(canvas.namespaceURI, "text")!;
  attr(highscore,{
    x: 120,
    y: 20,
    fill: "white",
    class: "highscore"
  });
  highscore.textContent = "Highscore : 0";
  canvas.appendChild(highscore);
  // Create a stage counter
  const stage = document.createElementNS(canvas.namespaceURI, "text")!;
  attr(stage,{
    x: 260,
    y: 20,
    fill: "white",
    class: "stage"
  });
  stage.textContent = "Stage : 1";
  canvas.appendChild(stage);
  // Create a charge counter
  const charge = document.createElementNS(canvas.namespaceURI, "text")!;
  attr(charge,{
    x: 360,
    y: 20,
    fill: "white",
    class: "charge"
  });
  stage.textContent = "Charge : 1";
  canvas.appendChild(charge);

  // Set up controls
  const 
    observeKey = <T>(eventName:string, k:Key, result:()=>T)=>
    fromEvent<KeyboardEvent>(document,eventName)
      .pipe(
        filter(({code})=>code === k),
        filter(({repeat})=>!repeat),
        map(result)
      ),

    startLeftMove = observeKey('keydown','ArrowLeft',()=>new Move(-2)),
    startRightMove = observeKey('keydown','ArrowRight',()=>new Move(2)),
    stopLeftMove = observeKey('keyup','ArrowLeft',()=>new Move(0)),
    stopRightMove = observeKey('keyup','ArrowRight',()=>new Move(0)),
    shoot = observeKey('keydown','Space', ()=>new Shoot()),
    ult = observeKey('keydown','KeyE', ()=>new Ult())

  // Create type for all units in the game
  type Body = Readonly<{
    id: string
    x: number;
    y: number;
    xdirection: number;
    ydirection: number;
    createTime: number
  }>
  // Create type for the game state
  type State = Readonly<{
    time:number,
    ship:Body,
    bullets:ReadonlyArray<Body>,
    aliens:ReadonlyArray<Body>,
    exit:ReadonlyArray<Body>,
    gameOver:Boolean,
    objCount:number,
    shields:ReadonlyArray<Body>,
    score: number,
    timeoffset: number,
    gameWin: Boolean,
    highScore: number,
    stage: number,
    charge: number,
    laser: ReadonlyArray<Body>
  }>

  // Some constant values in the game
  const 
  CONSTANTS = {
    CANVAS_SIZE: 600,
    BULLET_EXPIRATION_TIME: 170,
    START_ALIENS_ROW: 5,
    START_ALIENS_COLUMN: 10,
    BULET_RADIUS: 3,
    BULLET_VELOCITY: 3,
    START_TIME: 0,
    ALIEN_SIZE: 10,
    ALIEN_SPEED: 0.5,
    SHIELD_PIECES: 8
  } as const

  // Set up initial aliens array and shields array
  const 
    startAliens = [...Array(CONSTANTS.START_ALIENS_COLUMN)]
    .map((_,i)=>createUnit("alien"+String(i),20+50*i,50))
    .concat([...Array(CONSTANTS.START_ALIENS_COLUMN)]
    .map((_,i)=>createUnit("alien"+String(i+10),20+50*i,50+50)))
    .concat([...Array(CONSTANTS.START_ALIENS_COLUMN)]
    .map((_,i)=>createUnit("alien"+String(i+20),20+50*i,50+100)))
    .concat([...Array(CONSTANTS.START_ALIENS_COLUMN)]
    .map((_,i)=>createUnit("alien"+String(i+30),20+50*i,50+150)))
    .concat([...Array(CONSTANTS.START_ALIENS_COLUMN)]
    .map((_,i)=>createUnit("alien"+String(i+40),20+50*i,50+200)))
    .map((x)=><Body>{...x,xdirection: CONSTANTS.ALIEN_SPEED}),

    startShields = createShield(75,400,0).concat(createShield(250,400,8)).concat(createShield(425,400,16))
    .concat(createShield(75,410,24)).concat(createShield(250,410,32)).concat(createShield(425,410,40))
    .concat(createShield(75,420,48)).concat(createShield(250,420,56)).concat(createShield(425,420,64))
    .concat(createShield(75,430,72)).concat(createShield(250,430,80)).concat(createShield(425,430,88))
    .concat(createShield(75,440,96)).concat(createShield(250,440,104)).concat(createShield(425,440,112))

  // Initialise initial game state
  const initialState:State = {
    time: CONSTANTS.START_TIME,
    ship: createUnit('ship',290,500),
    bullets: [],
    exit: [],
    aliens: startAliens,
    gameOver: false,
    objCount: 0,
    shields: startShields,
    score: 0,
    timeoffset: 0,
    gameWin: false,
    highScore: 0,
    stage: 1,
    charge: 0,
    laser: []
  }
  
  // Function to update the view of the game
  function updateView(state:State): void 
  {
    // draw all aliens
    state.aliens.forEach(alien=>{
      const createAlienView = ()=>{
        const element = document.createElementNS(canvas.namespaceURI,'polygon')
        element.setAttribute("id",alien.id);
        element.classList.add("alien")
        canvas.appendChild(element)
        return element;
      }      
      const element = document.getElementById(alien.id) || createAlienView();
      element.setAttribute("points", 
      String(alien.x-4)+","+String(alien.y-9)+" "+
      String(alien.x-2)+","+String(alien.y-3)+" "+
      String(alien.x+2)+","+String(alien.y-3)+" "+
      String(alien.x+4)+","+String(alien.y-9)+" "+
      String(alien.x+9)+","+String(alien.y-5)+" "+
      String(alien.x+9)+","+String(alien.y+9)+" "+
      String(alien.x+5)+","+String(alien.y+9)+" "+
      String(alien.x+5)+","+String(alien.y+2)+" "+
      String(alien.x+2)+","+String(alien.y+2)+" "+
      String(alien.x+2)+","+String(alien.y+6)+" "+
      String(alien.x-2)+","+String(alien.y+6)+" "+
      String(alien.x-2)+","+String(alien.y+2)+" "+
      String(alien.x-5)+","+String(alien.y+2)+" "+
      String(alien.x-5)+","+String(alien.y+9)+" "+
      String(alien.x-9)+","+String(alien.y+9)+" "+
      String(alien.x-9)+","+String(alien.y-5)+" ");
      element.setAttribute("width",String(CONSTANTS.ALIEN_SIZE))
      element.setAttribute("height",String(CONSTANTS.ALIEN_SIZE))
      element.setAttribute("fill","red")
    })

    // draw all shields
    state.shields.forEach(shield=>{
      const createAlienView = ()=>{
        const element = document.createElementNS(canvas.namespaceURI,'rect')
        element.setAttribute("id",shield.id);
        element.classList.add("shield")
        canvas.appendChild(element)
        return element;
      }
      const element = document.getElementById(shield.id) || createAlienView();
      element.setAttribute("x",String(shield.x))
      element.setAttribute("y",String(shield.y))
      element.setAttribute("width",String(10))
      element.setAttribute("height",String(10))
      element.setAttribute("fill","green")
    })

    // draw all bullets
    state.bullets.forEach(bullet=>{
      const createBulletView = ()=>{
        const element = document.createElementNS(canvas.namespaceURI, "ellipse")!;
        element.setAttribute("id",bullet.id);
        element.classList.add("bullet")
        canvas.appendChild(element)
        return element;
      }
      const element = document.getElementById(bullet.id) || createBulletView();
      element.setAttribute("cx",String(bullet.x))
      element.setAttribute("cy",String(bullet.y))
      element.setAttribute("rx",String(CONSTANTS.BULET_RADIUS))
      element.setAttribute("ry",String(CONSTANTS.BULET_RADIUS*2))
      element.setAttribute("fill",bullet.ydirection > 0 ? "yellow" : "white")
    })

    // functions draw laser
    state.laser.forEach(laser=>{
      const createLaserView = () => {
        const element = document.createElementNS(canvas.namespaceURI, "rect")!;
        element.setAttribute("id","laser");
        element.classList.add("laser")
        canvas.appendChild(element)
        return element;
      }
      const element = document.getElementById("laser") || createLaserView();
      element.setAttribute("x",String(state.ship.x-CONSTANTS.ALIEN_SIZE))
      element.setAttribute("y",String(0))
      element.setAttribute("width",String(CONSTANTS.ALIEN_SIZE*2))
      element.setAttribute("height",String(state.ship.y-CONSTANTS.ALIEN_SIZE))
      element.setAttribute("fill","white")
    })

    // remove unwanted/dead bodies from the view
    state.exit.forEach(o=>{
      const element = document.getElementById(o.id);
      element? canvas.removeChild(element) : undefined
    })

    // update view of the ship
    ship.setAttribute('transform',`translate(${state.ship.x},${state.ship.y})`)
    // update view for current score, highscore and stage
    score.textContent = "Score : " + String(state.score)
    highscore.textContent = "Highscore : " + String(state.highScore)
    stage.textContent = "Stage : " + String(state.stage)
    charge.textContent = "Charge : " + String(state.charge)

    // if ship is dead, unsubscribe to the stream and print game over, then restarts the game
    if(state.gameOver) {
      const element = document.createElementNS(canvas.namespaceURI, "text")!;
      attr(element,{
        x: CONSTANTS.CANVAS_SIZE/2 -150,
        y: CONSTANTS.CANVAS_SIZE/2,
        fill: "white",
        class: "gameover"
      });

      element.textContent = "Game Over ! Restarting the game...";
      canvas.appendChild(element);
      setTimeout(() => {
        canvas.removeChild(element)
      }, 3000);
    }

    // if no more aliens are left, unsubscribe to the stream and print congratulations message 
    if(state.aliens.length == 0){
      const element = document.createElementNS(canvas.namespaceURI, "text")!;
      attr(element,{
        x: CONSTANTS.CANVAS_SIZE/2 -130,
        y: CONSTANTS.CANVAS_SIZE/2,
        fill: "white",
        class: "win"
      });
      element.textContent = "You Won ! Entering next stage...";
      canvas.appendChild(element);
      setTimeout(() => {
        canvas.removeChild(element)
      }, 3000);
    }
  }

  // State transducer function
  const reduceState = (s:State, e:Move|Tick)=>
    // change direction of ship according to the user input
    e instanceof Move ? 
    {
      ...s,
      ship: {...s.ship,xdirection:e.direction}
    } 
    : 
    // shoots a bullet from the ship according to user input
    e instanceof Shoot ? 
    {...s,
      bullets: s.bullets.concat([createBullet(s)(-1)(s.ship.x)(s.ship.y-15)]),
      objCount: s.objCount + 1
    } 
    : 
    e instanceof Ult ?
      s.charge >= 10 ? laser(s) : s
    : 
    // perform other game related functions
    tick(s,e.elapsed);

  const laser = (s:State) => {
    return {
      ...s,
      aliens: s.aliens.filter((aliens)=>(Math.abs(aliens.x-s.ship.x))>15),
      bullets: s.bullets.filter((aliens)=>(Math.abs(aliens.x-s.ship.x))>15),
      charge: s.charge >= 10 ? 0 : s.charge,
      shields: s.shields.filter((aliens)=>(Math.abs(aliens.x-s.ship.x+5))>15),
      exit: s.aliens.filter((aliens)=>(Math.abs(aliens.x-s.ship.x))<=15)
      .concat(s.bullets.filter((aliens)=>(Math.abs(aliens.x-s.ship.x))<=15))
      .concat(s.shields.filter((aliens)=>(Math.abs(aliens.x-s.ship.x+5))<=15)),
      laser: [createLaser(s)]
    }
  }
  
  // Move the object based on its xdirection and ydirection
  const moveObj = (o:Body) => <Body>
    {
    ...o,
    x : o.x > canvas.clientWidth - 15 ?
      o.x - 1
      : 
      o.x < 15 ?
      o.x + 1
      :
      o.x + o.xdirection,
    y : o.y + o.ydirection
    }

  // Function to check for collisions
  const handleCollisions = (s:State) => {
    const 
      cut = except((a:Body)=>(b:Body)=>a.id === b.id),
      // check if any alien and bullet collided
      aliensAndBulletsCollided = ([bullet,alien]:[Body,Body]) => 
        Math.abs((bullet.x)-alien.x)<CONSTANTS.ALIEN_SIZE 
        && Math.abs((bullet.y)-alien.y)<CONSTANTS.ALIEN_SIZE
        && alien.id.includes("alien")
        && bullet.ydirection == -3,

      // store collided aliens and bullets in arrays
      allBulletsAndAliens = s.bullets.flatMap((bullet)=>s.aliens.map(alien=>([bullet,alien]))),
      collidedBulletsAndAliens = allBulletsAndAliens.filter(aliensAndBulletsCollided),
      collidedBullets1 = collidedBulletsAndAliens.map(([bullet,_])=>bullet),
      collidedAliens1 = collidedBulletsAndAliens.map(([_,alien])=>alien),
  
      // check if any bullet collided with the ship or shield
      shipAndBulletsCollided = ([bullet,body]:[Body,Body]) => 
        Math.abs(body.x-bullet.x)<10
        && Math.abs(body.y-bullet.y)<10,

      // store if any bullet collided with the ship in a boolean variable
      shipCollidedBullet = s.bullets.filter(bullet=>shipAndBulletsCollided([bullet,s.ship])).length > 0,

      shieldAndBulletsCollided = ([bullet,body]:[Body,Body]) => 
        Math.abs(body.x-bullet.x+5)<7
        && Math.abs(body.y-bullet.y+5)<7,
      
      // store collided bullet and shield in arrays
      allBulletsAndShields = s.bullets.flatMap((bullet)=>s.shields.map(shields=>([bullet,shields]))),
      collidedBulletsAndShields = allBulletsAndShields.filter(shieldAndBulletsCollided),
      collidedBullets2 = collidedBulletsAndShields.map(([bullet,_])=>bullet),
      collidedShields2 = collidedBulletsAndShields.map(([_,shields])=>shields),

      // check if alien collided with shield or the ship
      aliensAndOthersCollided = ([body,alien]:[Body,Body]) => 
      Math.abs((body.x)-alien.x)<CONSTANTS.ALIEN_SIZE*2 
      && Math.abs((body.y)-alien.y)<CONSTANTS.ALIEN_SIZE*2,

      // store collided aliens and shields in arrays
      allAliensAndShields = s.shields.flatMap((shields)=>s.aliens.map(alien=>([shields,alien]))),
      collidedAliensAndShields = allAliensAndShields.filter(aliensAndOthersCollided),
      collidedAliens2 = collidedAliensAndShields.map(([_,alien])=>alien),
      collidedShields1 = collidedAliensAndShields.map(([shield,_])=>shield),

      // check if any alien moved pass the ship
      baseLineCollidedAlien = s.aliens.filter(alien=>alien.y > s.ship.y).length > 0,

      // check if any alien collided with the ship
      shipCollidedAlien = s.aliens.filter(alien=>aliensAndOthersCollided([s.ship,alien])).length > 0,
      // update if ship has collided with anything
      shipCollided =  shipCollidedBullet || shipCollidedAlien || baseLineCollidedAlien,

      // check collisions between bullets
      bulletsAndBulletsCollided = ([bullet1,bullet2]:[Body,Body]) => 
      Math.abs(bullet2.x-bullet1.x)<=4 && Math.abs(bullet2.x-bullet1.x)>0
      && Math.abs(bullet2.y-bullet1.y)<=4 && Math.abs(bullet2.y-bullet1.y)>0,

      // store collided bullets the arrays
      allBullets = s.bullets.flatMap((bullet1)=>s.bullets.map(bullet2=>([bullet1,bullet2]))),
      collidedBulletsAndBullets = allBullets.filter(bulletsAndBulletsCollided),
      collidedBullets3 = collidedBulletsAndBullets.map(([bullet,_])=>bullet),
      collidedBullets4 = collidedBulletsAndBullets.map(([_,bullet])=>bullet),
      
      // create arrays containing all collided bodies
      collidedBullets = collidedBullets1.concat(collidedBullets2).concat(collidedBullets3).concat(collidedBullets4),
      collidedAliens = collidedAliens1.concat(collidedAliens2),
      collidedShields = collidedShields1.concat(collidedShields2)


    // return a state such that all collided units are removed
    return <State>{
      ...s,
      bullets: cut(s.bullets)(collidedBullets),
      aliens: cut(s.aliens)(collidedAliens),
      shields: cut(s.shields)(collidedShields),
      exit: s.exit.concat(collidedBullets,collidedAliens,collidedShields),
      objCount: s.objCount,
      gameOver: shipCollided,
      score: s.score + collidedAliens.length,
      charge: s.charge < 10 ? s.charge + collidedAliens.length : s.charge
    }
  }

  // Function that processes the logics of the games
  const tick = (s:State,elapsed:number) => {
    // find expired bullets and laser
    const 
      not = <T>(f:(x:T)=>boolean)=>(x:T)=>!f(x),
      expired = (b:Body)=>(elapsed - s.timeoffset - b.createTime) > CONSTANTS.BULLET_EXPIRATION_TIME,
      expiredBullets:Body[] = s.bullets.filter(expired),
      activeBullets = s.bullets.filter(not(expired)),
      laserTimer = s.laser.filter((laser)=>((elapsed - s.timeoffset - laser.createTime)) > 7);
    
    // declare functions used to move aliens
    const changeX = (b:Body) => <Body> {...b,xdirection: b.xdirection*-1}
    const changeY = (b:Body) => <Body> {...b,y: b.y+3*s.stage}

    // function allow aliens to create bullets and shoot toward the player
    function alienShoot (s:State){
      const currentAlien = s.aliens[Math.floor(Math.random() * s.aliens.length)];
      const bullet = s.aliens.length > 0 ?createBullet(s)(1)(currentAlien.x)(currentAlien.y+CONSTANTS.ALIEN_SIZE) : []
      return (s.time % (Math.floor(15/s.stage)+1)) != 0 ?       
      <State>{
        ...s,
        objCount: s.objCount,
        bullets: []
      }
      :
      <State>{
        ...s,
        objCount: s.objCount+1,
        bullets: [bullet]
      }
    }

    const doNothing = (alien:Body) => alien

    // update the coordinates of the aliens
    const alienState = s.aliens.map(moveObj).map(s.time%200==199?changeX:doNothing).map(s.time%200==199?changeY:doNothing)

    return s.gameOver ? 
    // remove all units and reset the game if the player has lost
    {
      ...initialState, 
      exit: s.bullets.concat(s.aliens).concat(s.shields),
      timeoffset: s.time+s.timeoffset,
      highScore: s.score > s.highScore ? s.score : s.highScore
    }
    :
    // remove all units, increment the stage count, and enter the next stage of the game if the player has killed all aliens
    s.gameWin ?
    {
      ...initialState, 
      exit: s.bullets.concat(s.aliens).concat(s.shields).concat(s.laser),
      timeoffset: s.time+s.timeoffset,
      score: s.score,
      highScore: s.score > s.highScore ? s.score : s.highScore,
      stage: s.stage+1
    }
    :
    // else update the current game state
    handleCollisions({...s, 
      objCount: alienShoot(s).objCount,
      ship:moveObj(s.ship), 
      bullets:activeBullets.concat(alienShoot(s).bullets).map(moveObj),
      exit:expiredBullets.concat(laserTimer),
      aliens: alienState,
      time:elapsed - s.timeoffset,
      gameWin: s.aliens.length==0,
      laser: s.laser
    })
  }

  // Function to create a bullet
  const createBullet = (s:State) => (direction:number) => (x:number) => (y:number) => <Body> {
      id: `bullet${s.objCount}`,
      x: x,
      y: y,
      createTime: s.time,
      xdirection: 0,
      ydirection: CONSTANTS.BULLET_VELOCITY*direction
  }

  // Function to create a unit
  function createUnit(id : string,x:number,y:number):Body {
    return {
      id: id,
      x: x,
      y: y,
      xdirection: 0,
      ydirection: 0,
      createTime:0
    }
  }

  // Function to create a laser
  function createLaser(s:State):Body {
    return {
      id: "laser",
      x: s.ship.x-CONSTANTS.ALIEN_SIZE,
      y: 0,
      xdirection: 0,
      ydirection: 0,
      createTime: s.time
    }
  }

  // Function to create a row of shields
  function createShield(x:number,y:number,index:number):ReadonlyArray<Body>{
    return [...Array(CONSTANTS.SHIELD_PIECES)]
    .map((_,i)=>createUnit("shield"+(String(i+index)),x+10*i,y))
  }

  // main subscription stream
  const subscription = 
  interval(10)
  .pipe
  (
    map(elapsed=>new Tick(elapsed)),
    merge(startLeftMove,startRightMove,stopLeftMove,stopRightMove,shoot,ult),
    scan(reduceState, initialState)
  )
  .subscribe(updateView);
}

  // the following simply runs your pong function on window load.  Make sure to leave it in place.
  if (typeof window != 'undefined'){
    window.onload = ()=>{
      spaceinvaders();
    }
  }
  
  const 
  /**
   * Composable not: invert boolean result of given function
   * @param f a function returning boolean
   * @param x the value that will be tested with f
   */
    not = <T>(f:(x:T)=>boolean)=> (x:T)=> !f(x),
  /**
   * is e an element of a using the eq function to test equality?
   * @param eq equality test function for two Ts
   * @param a an array that will be searched
   * @param e an element to search a for
   */
    elem = 
      <T>(eq: (_:T)=>(_:T)=>boolean)=> 
        (a:ReadonlyArray<T>)=> 
          (e:T)=> a.findIndex(eq(e)) >= 0,
  /**
   * array a except anything in b
   * @param eq equality test function for two Ts
   * @param a array to be filtered
   * @param b array of elements to be filtered out of a
   */ 
    except = 
      <T>(eq: (_:T)=>(_:T)=>boolean)=>
        (a:ReadonlyArray<T>)=> 
          (b:ReadonlyArray<T>)=> a.filter(not(elem(eq)(b))),
    /**
     * set a number of attributes on an Element at once
     * @param e the Element
     * @param o a property bag
     */         
    attr = (e:Element,o:Object) =>
    { for(const k in o) e.setAttribute(k,String(o[k])) }
  

