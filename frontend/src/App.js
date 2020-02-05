import React from 'react';
import logo from './logo.svg';
import './App.css';
import CommitList from './components/CommitList'
import Typography from '@material-ui/core/Typography';
import 'typeface-roboto';
import { makeStyles } from '@material-ui/core/styles';

// Color scheme: https://coolors.co/06aed5-086788-f0c808-fff1d0-dd1c1a


const useStyles = makeStyles(theme => ({
  title: {
    display: 'inline',
    color: '#F0C808',
    fontFamily: 'Roboto',
  }
}));

function App() {
  const classes = useStyles();

  return (
    <div className="App">
      <header className="App-header">
        <React.Fragment>
          <Typography
                  component="span"
                  variant="h2"
                  className={classes.title}
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
