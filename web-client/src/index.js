import React from 'react';
//import ReactDOM from 'react-dom';
import {createRoot} from 'react-dom/client';
import './index.css';
import CharityCaseApp from "./CharityCaseApp";


const container=document.getElementById('root');
const root=createRoot(container);
root.render( <CharityCaseApp/>);

/*
ReactDOM.render(
    <div>
        <CharityCaseApp/>
    </div>,
    document.getElementById('root')
);*/
