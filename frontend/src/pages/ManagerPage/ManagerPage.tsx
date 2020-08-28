import React, {Component, ReactNode} from "react";
import './ManagerPage.css';

import TopBottomNumbering from "../../component/TopBottomNumbering/TopBottomNumbering";
import Options from "../../component/Options/Options";
import Numbering from "../../model/Numbering";
import Backend from "../../service/Backend";

type Props = {
    lastServed :Numbering
}
type State = {
    timeType :string
}
export default class ManagerPage extends Component<Props, State>{
    public state :State;

    constructor(props :Props){
        super(props);
        this.state = {
            timeType : "served"
        }
    }

    public render() :ReactNode{
        return(
            <div className="ManagerPage">
                <div className={"last-served"}>
                    {this.props.lastServed ?
                        <TopBottomNumbering text={"SENHA EM ATENDIMENTO"} timeType={"served"} numbering={this.props.lastServed}></TopBottomNumbering>
                    : null}
                </div>

                <div className="options-container">
                    <Options headerText={"OPÇÕES"} button1Text={"PRÓXIMA SENHA"} button1Action={async () => {this.serveNext()}} button1HexColor={"#0E5880"} button2Text={"REINICIAR NUMERAÇÃO"} button2Action={async () => {this.resetNumbering()}} button2HexColor={"#0E5880"}></Options>
                </div>


            </div>
        )
    }

    public serveNext(){
        Backend.serveNext().then(numbering => {
            //alert(`Senha ${numbering.getFullNumberingCode()} chamada!`);
        }).catch(err => {
            alert(`Não foi possível chamar a próxima senha. O motivo informado pelo servidor é ${err}`);
        })
    }

    public resetNumbering(){
        Backend.resetNumbering().then(ok => {
            if(ok){
                alert(`Numeração de senhas reiniciada com sucesso!`);
            }else{
                throw new Error();
            }
        }).catch(err => {
            alert(`Não foi possível reiniciar a numeração das senhas. Tente novamente em alguns instantes.`);
        })
    }
}
