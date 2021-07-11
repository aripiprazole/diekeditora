import {AnyAction} from 'redux';
import {AuthActions} from './modules/auth/actions';

const disabledLogsActions: string[] = [AuthActions.LOGIN];

export const logger = (_store: any) => (next: (action: AnyAction) => void) => (action: AnyAction) => {
  next(action);

  if (disabledLogsActions.includes(action.type)) {
    console.log('[REDUX]', action);
  }
};
