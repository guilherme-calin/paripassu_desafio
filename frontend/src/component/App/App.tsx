import React, {Component, ReactNode} from 'react';
import paripassu_logo from './assets/paripassu_logo.png';
import './App.css';
import {BrowserRouter, HashRouter, NavLink, Route, Switch} from 'react-router-dom';
import NumberingPage from "../../pages/NumberingPage/NumberingPage";
import ClientPage from "../../pages/ClientPage/ClientPage";
import ManagerPage from "../../pages/ManagerPage/ManagerPage";
import NotFoundPage from "../../pages/NotFoundPage/NotFoundPage";
import Numbering from "../../model/Numbering";
import Backend from "../../service/Backend";

type Props = {};
type State = {
    lastServed :Numbering[]
    lastGenerated :Numbering | null
}
export class App extends Component<Props, State> {
    public state :State;

    constructor(props :Props) {
        super(props);
        this.state = {
            lastServed : [],
            lastGenerated : null
        }
    }

    render() :ReactNode{
        return (
            <BrowserRouter basename={"/paripassu"}>
                <div className="App">
                    <div className="header">
                        <img src={paripassu_logo}/>
                        <ul className="navigation">
                            <li><NavLink to="/" exact>ACOMPANHAMENTO DE SENHAS</NavLink></li>
                            <li><NavLink to="/cliente" exact>ÁREA DO CLIENTE</NavLink></li>
                            <li><NavLink to="/gerente" exact>ÁREA DO GERENTE</NavLink></li>
                        </ul>
                    </div>

                    <main>
                        <Switch>
                            <Route path="/" exact component={() => <NumberingPage numberings={this.state.lastServed}/>}/>
                            <Route path="/cliente" exact component={() => <ClientPage parentCallback={this.getLastGenerated} lastGenerated={this.state.lastGenerated}/>}/>
                            <Route path="/gerente" exact component={() => <ManagerPage lastServed={this.state.lastServed[0]}/>}/>
                            <Route component={NotFoundPage}/>
                        </Switch>
                    </main>

                    <div className="footer">
                        <span>Desenvolvido por Guilherme Suardi Calin</span>
                    </div>
                </div>
            </BrowserRouter>
        );
    }

  public componentDidMount(){
    this.getContinuousRequest();
  }

  public shouldComponentUpdate = (nextProps: Readonly<Props>, nextState: Readonly<State>, nextContext: any): boolean => {
    return true;
  }

  public getContinuousRequest= async () : Promise<void> => {
      try{
          let newNumberingList :Numbering[];
          if(this.state.lastServed.length > 0){
              newNumberingList = await Backend.getLastServed();
          }else{
              newNumberingList = await Backend.getLastServed();
          }

          this.setState({lastServed : newNumberingList});
      }catch(err){
          console.log(err);
      }

      setTimeout(this.getContinuousRequest, 500);
  }

  public getLastGenerated = async (numbering :Numbering) => {
        this.setState({lastGenerated : numbering});
  }
}

export default App;
