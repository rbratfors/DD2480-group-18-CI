import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import Divider from '@material-ui/core/Divider';
import ListItemText from '@material-ui/core/ListItemText';
import ListItemAvatar from '@material-ui/core/ListItemAvatar';
import Avatar from '@material-ui/core/Avatar';
import Typography from '@material-ui/core/Typography';
import 'typeface-roboto';

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

  return (
    <List className={classes.root}>
      <ListItem alignItems="flex-start">
        <ListItemText
          primary={
          <React.Fragment>
            <Typography
                component="span"
                variant="h6"
                className={classes.inline}
                color="textPrimary"
            >
                Checks failed
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
                Compilation error
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
      </ListItem>
      <Divider variant="inset" component="li" />
      <ListItem alignItems="flex-start">
        <ListItemText
          primary={
            <React.Fragment>
                <Typography
                    component="span"
                    variant="h6"
                    className={classes.inline}
                    color="textPrimary"
                >
                    Checks passed
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
                Passed
              </Typography>
              <Typography
                component="span"
                variant="body2"
                className={classes.inline}
              >
                  {" - Great job, well done"}
              </Typography>
            </React.Fragment>
          }
        />
      </ListItem>
    </List>
  );
}