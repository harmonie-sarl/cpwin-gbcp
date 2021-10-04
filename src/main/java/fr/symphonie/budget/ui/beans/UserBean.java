package fr.symphonie.budget.ui.beans;

import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.ui.beans.pluri.DialogHelper;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.util.GenericAction;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.MsgEntry;
import fr.symphonie.util.model.User;

public class UserBean extends fr.symphonie.web.jsf.beans.UserBean {

	private static final long serialVersionUID = -7182565747305927899L;
	private static final Logger logger = LoggerFactory.getLogger(UserBean.class);
	public UserBean() {	
		super();
	}
	
	public void chooseUser() {
		if (logger.isDebugEnabled()) {
			logger.info("chooseUser() - start"); //$NON-NLS-1$
		}
		setAction(GenericAction.UPDATE);
		DialogHelper.openEnregUserDialog();
		if (logger.isDebugEnabled()) {
			logger.debug("chooseUser() - end"); //$NON-NLS-1$
		}
	}
	
	
	public void closeEnregUserDialog() {
		if (logger.isDebugEnabled()) {
			logger.debug("closeEnregUserDialog() - start"); //$NON-NLS-1$
		}		
		DialogHelper.closeDialog(null);
		if (logger.isDebugEnabled()) {
			logger.debug("closeEnregUserDialog() - end"); //$NON-NLS-1$
		}
    }
	
    @Override
	public void setSelectedUser(User selectedUser) {
		super.setSelectedUser(selectedUser);
		if(selectedUser!=null)
			setSelectedRoleList(selectedUser.getRoleListView());	
	}

    /**
	 * Ajout ou modification d'un utilisateur dans la base de données
	 */
    @Override
	public void addOrUpdateUser(ActionEvent actionEvent) 
	{
    	try {
			upadetUserRoles();
			BudgetHelper.getCommonService().updateObject(getSelectedUser());
			HandlerJSFMessage.addInfo(MsgEntry.ENREG_USER_SUCCESS);
		} catch (Exception e) {
			HandlerJSFMessage.addExceptionMessage(e.getMessage());
			e.printStackTrace();
		}
	}


	@Override
	public void searchUsers() {		
		logger.info("search Users1 : login={} ,this.selectedName= ", getLogin(),getSelectedName()	);
			setUserList(getCommonService().getUserList(getLogin(), getSelectedName()));	
	}	
	
}
