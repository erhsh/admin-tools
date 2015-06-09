package com.erhsh.work.admintools.main;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.erhsh.work.admintools.service.IUserService;
import com.erhsh.work.admintools.service.impl.UserServiceImpl;
import com.erhsh.work.admintools.vo.DeviceVO;
import com.erhsh.work.admintools.vo.UserVO;

public class AdminToolsWinMain {
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

	protected Shell shell;
	private Text txt_target;
	private Table table;
	private TableViewer tableViewer;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AdminToolsWinMain window = new AdminToolsWinMain();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(761, 362);
		shell.setText("管理小工具");
		shell.setLayout(new GridLayout(1, false));

		Group grp_search = new Group(shell, SWT.NONE);
		GridLayout gl_grp_search = new GridLayout(2, false);
		gl_grp_search.verticalSpacing = 10;
		gl_grp_search.marginWidth = 10;
		gl_grp_search.marginTop = 10;
		gl_grp_search.marginRight = 10;
		gl_grp_search.marginLeft = 10;
		gl_grp_search.marginHeight = 10;
		gl_grp_search.marginBottom = 10;
		grp_search.setLayout(gl_grp_search);
		grp_search.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		grp_search.setText("搜索区");

		Label lbl_search = new Label(grp_search, SWT.NONE);
		lbl_search.setText("关键字");

		Composite cmp_search = new Composite(grp_search, SWT.NONE);
		cmp_search.setLayout(new GridLayout(2, false));
		cmp_search.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		txt_target = new Text(cmp_search, SWT.BORDER);
		txt_target.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Button btn_search = new Button(cmp_search, SWT.NONE);
		btn_search.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		btn_search.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Search Clicked");
				doGetUsers();
			}
		});
		btn_search.setText("搜索");

		Group grp_results = new Group(shell, SWT.NONE);
		grp_results.setLayout(new GridLayout(1, false));
		grp_results.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		grp_results.setText("查询结果");

		tableViewer = new TableViewer(grp_results, SWT.BORDER
				| SWT.FULL_SELECTION | SWT.MULTI);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TableViewerColumn tableViewerColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmn_id = tableViewerColumn.getColumn();
		tblclmn_id.setWidth(100);
		tblclmn_id.setText("用户ID");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmn_loginName = tableViewerColumn_1.getColumn();
		tblclmn_loginName.setWidth(100);
		tblclmn_loginName.setText("登录账号");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmn_nick = tableViewerColumn_2.getColumn();
		tblclmn_nick.setWidth(100);
		tblclmn_nick.setText("昵称");

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmn_failedLoginTimes = tableViewerColumn_3.getColumn();
		tblclmn_failedLoginTimes.setWidth(100);
		tblclmn_failedLoginTimes.setText("登录失败次数");

		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmn_devices = tableViewerColumn_4.getColumn();
		tblclmn_devices.setWidth(100);
		tblclmn_devices.setText("关联设备");

		Menu menu = new Menu(table);
		table.setMenu(menu);

		MenuItem menuItem_reset = new MenuItem(menu, SWT.CASCADE);
		menuItem_reset.setText("重置操作");

		Menu menu_reset = new Menu(menuItem_reset);
		menuItem_reset.setMenu(menu_reset);

		MenuItem menuItem_reset_1 = new MenuItem(menu_reset, SWT.NONE);
		menuItem_reset_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] selectItems = table.getSelection();
				for (TableItem selectItem : selectItems) {
					Object data = selectItem.getData();

					if (data instanceof UserVO) {
						IUserService userService = new UserServiceImpl();
						String id = ((UserVO) data).getId();
						System.out.println("resetFaildLoginTimes user, id= "
								+ id);
						userService.resetFaildLoginTimes(id);
						userService.destroy();
					}
				}
				doGetUsers();
			}
		});
		menuItem_reset_1.setText("清除登录失败记录");

		MenuItem menuItem_reset_2 = new MenuItem(menu_reset, SWT.NONE);
		menuItem_reset_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] selectItems = table.getSelection();
				for (TableItem selectItem : selectItems) {
					Object data = selectItem.getData();

					if (data instanceof UserVO) {
						IUserService userService = new UserServiceImpl();
						String id = ((UserVO) data).getId();
						System.out.println("cleanUserDeviceRels user, id= "
								+ id);
						userService.cleanUserDeviceRels(id);
						userService.destroy();
					}
				}
				doGetUsers();
			}
		});
		menuItem_reset_2.setText("删除用户绑定关系");

		MenuItem menuItem = new MenuItem(menu_reset, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
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
				doGetUsers();
			}
		});
		menuItem.setText("邮件去激活");

		new MenuItem(menu, SWT.SEPARATOR);

		MenuItem menuItem_delete = new MenuItem(menu, SWT.NONE);
		menuItem_delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
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
				doGetUsers();
			}
		});
		menuItem_delete.setText("删除用户");

		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn = tableViewerColumn_5.getColumn();
		tableColumn.setWidth(100);
		tableColumn.setText("邮箱激活");
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setContentProvider(new ContentProvider());

	}

	protected void doGetUsers() {

		try {
			IUserService userService = new UserServiceImpl();
			String target = txt_target.getText();
			List<UserVO> users = userService.getUsers(target);
			tableViewer.setInput(users);
			userService.destroy();

			System.out.println("Get User mail=" + target + ", result=" + users);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
