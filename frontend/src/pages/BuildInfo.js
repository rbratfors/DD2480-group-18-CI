import React from 'react';
import './../App.css';
import Build from '../components/Build'
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

export default function BuildInfo(props) {
  const classes = useStyles();
  const tempBuilds = props.builds;
  const build = tempBuilds[props.location.search.slice(4, props.location.search.length)];

  return (
      <header className="App-header">
        <React.Fragment>
          <Typography
                  component="span"
                  variant="h2"
                  className={classes.title}
          >
                  {!build ? "Loading id..." : build.jobID}
          </Typography>
        </React.Fragment>
        <Build {... props} />
      </header>
  );
}
