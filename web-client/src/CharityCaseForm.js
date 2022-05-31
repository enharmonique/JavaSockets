import React from 'react';

class CharityCaseForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {name: '', sum: ''};
    }

    handleNameChange = (event) => {
        this.setState({name: event.target.value});
    }

    handleSumChange = (event) => {
        this.setState({sum: event.target.value});
    }
    handleSubmit = (event) => {
        let charityCase = {
            id: this.state.id,
            name: this.state.name,
            sum: this.state.sum
        }
        console.log('A charity case was submitted: ');
        console.log(charityCase);
        this.props.addFunc(charityCase);
        event.preventDefault();
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <label>
                    Name:
                    <input type="text" value={this.state.name} onChange={this.handleNameChange}/>
                </label><br/>
                <label>
                    Sum:
                    <input type="string" value={this.state.sum} onChange={this.handleSumChange}/>
                </label><br/>
                <input type="submit" value="Add charity case"/>
            </form>
        );
    }
}

export default CharityCaseForm;