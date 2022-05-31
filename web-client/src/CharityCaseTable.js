import React from 'react';
import './CharityCaseApp.css'

class CharityCaseRow extends React.Component {
    handleDelete = (event) => {
        console.log('delete button pentru ' + this.props.charityCase.id);
        this.props.deleteFunc(this.props.charityCase.id);
    }

    render() {
        return (
            <tr>
                <td>{this.props.charityCase.id}</td>
                <td>{this.props.charityCase.name}</td>
                <td>{this.props.charityCase.sum}</td>
                <td>
                    <button onClick={this.handleDelete}>Delete</button>
                </td>
            </tr>
        );
    }
}

class CharityCaseTable extends React.Component {
    render() {
        let rows = [];
        let functieStergere = this.props.deleteFunc;
        this.props.charityCases.forEach(function (charityCase) {
            rows.push(<CharityCaseRow charityCase={charityCase} key={charityCase.id} deleteFunc={functieStergere}/>);
        });
        return (<div className="CharityCaseTable">
                <table className="center">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Sum</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>{rows}</tbody>
                </table>
            </div>
        );
    }
}

export default CharityCaseTable;