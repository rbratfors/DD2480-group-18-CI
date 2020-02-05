import React from 'react';
import logo from './logo.svg';
import './App.css';
import CommitList from './components/CommitList'
import Typography from '@material-ui/core/Typography';
import 'typeface-roboto';
import axios from 'axios';

// Color scheme: https://coolors.co/06aed5-086788-f0c808-fff1d0-dd1c1a

function App() {
  const proxy = 'https://thawing-brook-99242.herokuapp.com/'
  let test = axios.get(proxy + "http://038878d3.ngrok.io/ci/get")
    .then((res) => {
      console.log(res);
    })
  return (
    <div className="App">
      <header className="App-header">
        <React.Fragment>
          <Typography
                  component="span"
                  variant="h2"
                  className={{
                    display: 'inline',
                    color: '#06AED5',
                    fontFamily: 'Roboto'
                  }}
          >
                  {"CI build list"}
          </Typography>
        </React.Fragment>
        <CommitList />
      </header>
    </div>
  );
}

export default App;
