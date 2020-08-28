import React, {Component, ReactNode} from "react";
import './ClientPage.css';

import TopBottomNumbering from "../../component/TopBottomNumbering/TopBottomNumbering";
import Options from "../../component/Options/Options";
import Numbering from "../../model/Numbering";
import Backend from "../../service/Backend";

type Props = {
    parentCallback :(numbering :Numbering) => Promise<void>,
    lastGenerated :Numbering | null
}
type State = {
    timeType :string
}
export default class ClientPage extends Component<Props, State>{
    public state :State;

    constructor(props :Props){
        super(props);
        this.state = {
            timeType : "served"
        }
    }

    public render() :ReactNode{
        return(
            <div className="ClientPage">
                <div className={"last-generated"}>
                    {this.props.lastGenerated ?
                        <TopBottomNumbering text={"ÚLTIMA SENHA GERADA"} timeType={"request"} numbering={this.props.lastGenerated}></TopBottomNumbering>
                    : null}
                </div>

                <div className="options-container">
                    <Options headerText={"GERAR SENHA"} button1Text={"NORMAL"} button1Action={async () => {this.generateNumbering("N")}} button1HexColor={"#0E5880"} button2Text={"PRIORITÁRIA"} button2Action={async () => {this.generateNumbering("P")}} button2HexColor={"#551A4F"}></Options>
                </div>


            </div>
        )
    }

    public shouldComponentUpdate(nextProps: Readonly<Props>, nextState: Readonly<State>, nextContext: any): boolean {
        if(nextProps.lastGenerated){
            return true;
        }else{
            return false;
        }
    }

    public generateNumbering = (numberingType :string) :Promise<void> => {
        return new Promise((resolve, reject) => {
            Backend.generateNumbering(numberingType).then(numbering => {
                this.props.parentCallback(numbering);
                resolve();
            }).catch(err => {
                console.log(err);
                alert("Não foi possível gerar uma nova senha! Tente novamente mais tarde.");
                reject();
            });
        })
    }
}
