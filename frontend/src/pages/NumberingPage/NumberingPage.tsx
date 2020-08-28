import React, {Component, ReactNode} from "react";
import './NumberingPage.css';

import LeftBottomNumbering from "../../component/LeftBottomNumbering/LeftBottomNumbering";
import Numbering from "../../model/Numbering";

import loading from "./assets/loading.svg"

type Props = {
    numberings :Numbering[]
}
type State = {
    maxQueueItems :number,
    timeType :string
}
export default class NumberingPage extends Component<Props, State>{
    public state :State;

    constructor(props :Props){
        super(props);
        this.state = {
            maxQueueItems : 4,
            timeType : "served"
        }
    }

    public render() :ReactNode{
        let currentNumbering :JSX.Element | null = null;
        let lastServed :JSX.Element[] = [];

        for(let i = 0; i < this.props.numberings.length; i++){
            if(i === 0){
                currentNumbering = <LeftBottomNumbering identifier={(i+1).toString()} timeType={this.state.timeType} numbering={this.props.numberings[i]}></LeftBottomNumbering>;
            }else{
                if(lastServed.length < this.state.maxQueueItems - 1){
                    lastServed.push(<div className={"last"} key={i + 1}><LeftBottomNumbering identifier={(i+1).toString()} timeType={this.state.timeType} numbering={this.props.numberings[i]}></LeftBottomNumbering></div>);
                }
            }
        }
        return(
            this.props.numberings.length > 0 ?
            <div className="NumberingPage">
                <div className="current">
                    {currentNumbering}
                </div>
                {
                    this.props.numberings.length > 1 ?
                    <div className="last-container">
                        {lastServed}
                    </div>
                    : null
                }
            </div>
                :
                <div className="NumberingPage">
                    <img src={loading}></img>
                    <div className="loading">Carregando</div>
                </div>
        )
    }
}
