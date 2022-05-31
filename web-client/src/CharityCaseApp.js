import React from  'react';
import CharityCaseTable from './CharityCaseTable';
import './CharityCaseApp.css'
import CharityCaseForm from "./CharityCaseForm";
import {GetCharityCases, DeleteCharityCase, AddCharityCase} from './utils/rest-calls'


class CharityCaseApp extends React.Component{
    constructor(props){
        super(props);
        this.state={charityCases:[{"name":"testCharityCase","sum":"888","id":"10"}],
            deleteFunc:this.deleteFunc.bind(this),
            addFunc:this.addFunc.bind(this),
        }
        console.log('CharityCaseApp constructor')
    }

    addFunc(charityCase){
        console.log('inside add Func '+charityCase);
        AddCharityCase(charityCase)
            .then(res=> GetCharityCases())
            .then(charityCases=>this.setState({charityCases}))
            .catch(erorr=>console.log('eroare add ',erorr));
    }


    deleteFunc(charityCase){
        console.log('inside deleteFunc '+charityCase);
        DeleteCharityCase(charityCase)
            .then(res=> GetCharityCases())
            .then(charityCases=>this.setState({charityCases}))
            .catch(error=>console.log('eroare delete', error));
    }

    componentDidMount(){
        console.log('inside componentDidMount')
        GetCharityCases().then(charityCases=>this.setState({charityCases}));
    }

    render(){
        return(
            <div className="CharityCaseApp">
                <h1>App CharityCase Management</h1>
                <CharityCaseForm addFunc={this.state.addFunc}/>
                <br/>
                <br/>
                <CharityCaseTable charityCases={this.state.charityCases} deleteFunc={this.state.deleteFunc}/>
            </div>
        );
    }
}

export default CharityCaseApp;