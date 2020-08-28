import React, {Component, ReactNode} from "react";
import './Options.css';

import loading from "./assets/loading.svg"

type Props = {
    headerText :string,
    button1Text :string,
    button1Action :() => Promise<void>,
    button1HexColor :string,
    button2Text :string,
    button2Action :() => Promise<void>,
    button2HexColor :string,
}

type State = {
    buttonEnabled :boolean
}
export default class Options extends Component<Props, State>{
    public state :State;

    constructor(props :Props){
        super(props);
        this.state = {
            buttonEnabled: true
        }
    }

    public render() :ReactNode{
        return(
            <div className="Options">
                <div className="header">
                    <span>{this.props.headerText}</span>
                </div>

                <div className="body">
                    <div className="button-one">
                        <button style={{backgroundColor : this.props.button1HexColor}} disabled={!this.state.buttonEnabled} onClick={this.onClick1}>
                            {this.state.buttonEnabled ?
                                this.props.button1Text
                                :
                                <img src={loading}/>
                            }

                        </button>
                    </div>
                    <div className="button-two">
                        <button style={{backgroundColor : this.props.button2HexColor}} disabled={!this.state.buttonEnabled} onClick={this.onClick2}>
                            {this.state.buttonEnabled ?
                                this.props.button2Text
                                :
                                <img src={loading}/>
                            }
                        </button>
                    </div>

                </div>
           </div>
        )
    }

    public onClick1 = (event: any) :void => {
        console.log("Clicou?");
        this.setState({buttonEnabled : false});
        this.props.button1Action();
    }

    public onClick2 = (event :any) :void => {
        this.setState({buttonEnabled : false});
        this.props.button2Action();
    }
}
