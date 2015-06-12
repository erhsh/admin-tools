package com.erhsh.work.admintools.main;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.erhsh.work.admintools.service.IUserService;
import com.erhsh.work.admintools.service.impl.UserServiceImpl;
import com.erhsh.work.admintools.vo.DeviceVO;
import com.erhsh.work.admintools.vo.UserVO;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class UserInfoComposite extends Composite {
	private class TableLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			String colText = "";

			UserVO userVO = (UserVO) element;

			switch (columnIndex) {
			case 0:
				colText = userVO.getId();
				break;
			case 1:
				colText = userVO.getLoginName();
				break;
			case 2:
				colText = userVO.getNick();
				break;
			case 3:
				colText = userVO.getFailedLoginTimes();
				break;
			case 4:
				List<DeviceVO> devices = userVO.getDevices();
				if (null == devices) {
					break;
				}
				for (DeviceVO device : devices) {
					colText = colText + "[" + device.getId() + ":"
							+ device.getMac() + "]";
				}
				break;
			case 5:
				colText = userVO.getEmailAvailable();
				break;
			case 6:
				List<UserVO> parents = userVO.getParents();
				if (null == parents) {
					break;
				}
				colText = parents.toString();
				break;
			case 7:
				List<UserVO> children = userVO.getChildren();
				if (null == children) {
					break;
				}
				colText = children.toString();
				break;
			default:
				break;
			}

			return colText;
		}
	}

	private static class ContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List<?>) {
				return ((List<?>) inputElement).toArray();
			}
			return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	private Text txt_target;
	private Table table;
	private TableViewer tableViewer;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public UserInfoComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		Group grp_search = new Group(this, SWT.NONE);
		grp_search.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		grp_search.setText("搜索区");
		grp_search.setBounds(0, 0, 735, 97);
		GridLayout gl_grp_search = new GridLayout(2, false);
		gl_grp_search.verticalSpacing = 10;
		gl_grp_search.marginWidth = 10;
		gl_grp_search.marginTop = 10;
		gl_grp_search.marginRight = 10;
		gl_grp_search.marginLeft = 10;
		gl_grp_search.marginHeight = 10;
		gl_grp_search.marginBottom = 10;
		grp_search.setLayout(gl_grp_search);

		Label label = new Label(grp_search, SWT.NONE);
		label.setText("关键字");

		Composite composite = new Composite(grp_search, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		composite.setLayout(new GridLayout(2, false));

		txt_target = new Text(composite, SWT.BORDER);
		txt_target.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					doGetUsers();
				}
			}
		});
		txt_target.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Button button = new Button(composite, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doGetUsers();
			}
		});
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		button.setText("搜索");

		Group grp_result = new Group(this, SWT.NONE);
		grp_result.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		grp_result.setText("查询结果");
		grp_result.setLayout(new GridLayout(1, false));

		tableViewer = new TableViewer(grp_result, SWT.BORDER
				| SWT.FULL_SELECTION | SWT.MULTI);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TableViewerColumn tableViewerColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn = tableViewerColumn.getColumn();
		tableColumn.setWidth(100);
		tableColumn.setText("用户ID");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_1 = tableViewerColumn_1.getColumn();
		tableColumn_1.setWidth(100);
		tableColumn_1.setText("登录账号");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_2 = tableViewerColumn_2.getColumn();
		tableColumn_2.setWidth(100);
		tableColumn_2.setText("昵称");

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_3 = tableViewerColumn_3.getColumn();
		tableColumn_3.setWidth(100);
		tableColumn_3.setText("登录失败次数");

		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_4 = tableViewerColumn_4.getColumn();
		tableColumn_4.setWidth(100);
		tableColumn_4.setText("关联设备");

		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_5 = tableViewerColumn_5.getColumn();
		tableColumn_5.setWidth(100);
		tableColumn_5.setText("邮箱激活");

		Menu menu = new Menu(table);
		table.setMenu(menu);

		MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);
		menuItem.setText("重置操作");

		Menu menu_1 = new Menu(menuItem);
		menuItem.setMenu(menu_1);

		MenuItem menuItem_1 = new MenuItem(menu_1, SWT.NONE);
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearLoginFailedRecord();
				doGetUsers();
			}
		});
		menuItem_1.setText("清除登录失败记录");

		MenuItem menuItem_2 = new MenuItem(menu_1, SWT.NONE);
		menuItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				delUserDeviceRelation();
				doGetUsers();
			}
		});
		menuItem_2.setText("删除用户绑定关系");

		MenuItem menuItem_3 = new MenuItem(menu_1, SWT.NONE);
		menuItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearMailActiveRecord();
				doGetUsers();
			}
		});
		menuItem_3.setText("邮件去激活");

		MenuItem menuItem_4 = new MenuItem(menu, SWT.SEPARATOR);
		menuItem_4.setText("---分割线---");

		MenuItem menuItem_5 = new MenuItem(menu, SWT.NONE);
		menuItem_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				delUsers();
				doGetUsers();
			}
		});
		menuItem_5.setText("删除用户");

		TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmns = tableViewerColumn_6.getColumn();
		tblclmns.setWidth(100);
		tblclmns.setText("加入的家庭(s)");

		TableViewerColumn tableViewerColumn_7 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_6 = tableViewerColumn_7.getColumn();
		tableColumn_6.setWidth(100);
		tableColumn_6.setText("加入我的用户");
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setContentProvider(new ContentProvider());

	}

	protected void delUsers() {
		TableItem[] selectItems = table.getSelection();
		for (TableItem selectItem : selectItems) {
			Object data = selectItem.getData();

			if (data instanceof UserVO) {
				IUserService userService = new UserServiceImpl();
				String id = ((UserVO) data).getId();
				System.out.println("delUser user, id= " + id);
				userService.delUser(id);
				userService.destroy();
			}
		}
	}

	protected void clearMailActiveRecord() {
		TableItem[] selectItems = table.getSelection();
		for (TableItem selectItem : selectItems) {
			Object data = selectItem.getData();

			if (data instanceof UserVO) {
				IUserService userService = new UserServiceImpl();
				String id = ((UserVO) data).getId();
				System.out.println("cleanMailRegister user, id= " + id);
				userService.cleanMailRegister(id);
				userService.destroy();
			}
		}
	}

	protected void delUserDeviceRelation() {
		TableItem[] selectItems = table.getSelection();
		for (TableItem selectItem : selectItems) {
			Object data = selectItem.getData();

			if (data instanceof UserVO) {
				IUserService userService = new UserServiceImpl();
				String id = ((UserVO) data).getId();
				System.out.println("cleanUserDeviceRels user, id= " + id);
				userService.cleanUserDeviceRels(id);
				userService.destroy();
			}
		}

	}

	protected void clearLoginFailedRecord() {
		TableItem[] selectItems = table.getSelection();
		for (TableItem selectItem : selectItems) {
			Object data = selectItem.getData();

			if (data instanceof UserVO) {
				IUserService userService = new UserServiceImpl();
				String id = ((UserVO) data).getId();
				System.out.println("resetFaildLoginTimes user, id= " + id);
				userService.resetFaildLoginTimes(id);
				userService.destroy();
			}
		}
	}

	protected void doGetUsers() {

		try {
			IUserService userService = new UserServiceImpl();
			String target = txt_target.getText();

			List<UserVO> users = new ArrayList<UserVO>();
			UserVO user = userService.getUser(target);
			if (!user.isEmpty()) {
				users.add(user);
			} else {
				users = userService.getUsers(target);
			}

			tableViewer.setInput(users);
			userService.destroy();

			System.out.println("Get User mail=" + target + ", result=" + users);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
