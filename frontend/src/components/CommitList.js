import React, {useState, useEffect} from 'react';
import { makeStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import Divider from '@material-ui/core/Divider';
import ListItemText from '@material-ui/core/ListItemText';
import ListItemAvatar from '@material-ui/core/ListItemAvatar';
import Avatar from '@material-ui/core/Avatar';
import Typography from '@material-ui/core/Typography';
import 'typeface-roboto';
import axios from 'axios';

const useStyles = makeStyles(theme => ({
  root: {
    width: '100%',
    maxWidth: 360,
    backgroundColor: '#F0C808',
    borderRadius: 16,
    border: 2,
    boxShadow: '2px 5px 1px rgba(0, 0, 0, 0.15)'
  },
  inline: {
    display: 'inline',
    color: '#FFF1D0',
    fontFamily: 'Roboto'
  },
}));

export default function CommitList() {
  const classes = useStyles();
  const [builds, setState] = useState({});

  useEffect(() => {
    axios.get(process.env.REACT_APP_PROXY + process.env.REACT_APP_URL + "/ci/get")
    .then((res) => {
      setState(res['data'])
    })
    .catch((e) => {
      setState({});
    })

  }, [])


  return (
    <List className={classes.root}>
      {Object.keys(builds).map((key, index) => {
        return (<ListItem key={key} alignItems="flex-start">
          <ListItemText
            primary={
            <React.Fragment>
              <Typography
                  component="span"
                  variant="h6"
                  className={classes.inline}
                  color="textPrimary"
              >
                  {builds[key].status}
              </Typography>
            </React.Fragment>
          }
            secondary={
              <React.Fragment>
                <Typography
                  component="span"
                  variant="body2"
                  className={classes.inline}
                  color="textPrimary"
                >
                  {builds[key].commitSha}
                </Typography>
                <Typography
                  component="span"
                  variant="body2"
                  className={classes.inline}
                >
                    {" - blablabla javakod"}
                </Typography>
              </React.Fragment>
            }
          />
        </ListItem>)
      })}
    </List>
  );
}