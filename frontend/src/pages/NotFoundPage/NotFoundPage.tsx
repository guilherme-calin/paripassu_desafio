import React, {Component, ReactNode} from "react";
import './NotFoundPage.css';

import LeftBottomNumbering from "../../component/LeftBottomNumbering/LeftBottomNumbering";
import Numbering from "../../model/Numbering";

type Props = {

}
export default class NotFoundPage extends Component<Props>{
    public state :Object = {};

    constructor(props :Props){
        super(props);
    }

    public render() :ReactNode{
        return(
            <div className="NotFoundPage">
                <span>404</span>
                <span>Página não encontrada</span>
            </div>
        )
    }
}
