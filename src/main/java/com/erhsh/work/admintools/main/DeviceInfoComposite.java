package com.erhsh.work.admintools.main;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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

import com.erhsh.work.admintools.service.IDeviceService;
import com.erhsh.work.admintools.service.impl.DeviceServiceImpl;
import com.erhsh.work.admintools.vo.DeviceVO;
import com.erhsh.work.admintools.vo.UserVO;

public class DeviceInfoComposite extends Composite {
	private class TableLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			String colText = "";

			DeviceVO deviceVO = (DeviceVO) element;

			switch (columnIndex) {
			case 0:
				colText = deviceVO.getId();
				break;
			case 1:
				colText = deviceVO.getMac();
				break;
			case 2:
				colText = deviceVO.getMac2();
				break;
			case 3:
				colText = deviceVO.getName();
				break;
			case 4:
				colText = deviceVO.getDv();
				break;
			case 5:
				colText = deviceVO.getSn();
				break;
			case 6:
				colText = deviceVO.getAddr();
				break;
			case 7:
				colText = deviceVO.getCreateStamps();
				break;
			case 8:
				colText = (deviceVO.getOwner() == null || deviceVO.getOwner()
						.isEmpty()) ? "" : deviceVO.getOwner().toString();
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

	private Text text;
	private Table table;
	private TableViewer tableViewer;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public DeviceInfoComposite(Composite parent, int style) {
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

		text = new Text(composite, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					doGetDevices();
				}
			}
		});

		Button button = new Button(composite, SWT.NONE);
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doGetDevices();
			}
		});
		button.setText("搜索");

		Group grp_result = new Group(this, SWT.NONE);
		grp_result.setLayout(new GridLayout(1, false));
		grp_result.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		grp_result.setText("展示区");

		tableViewer = new TableViewer(grp_result, SWT.BORDER
				| SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TableViewerColumn tableViewerColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmn_devId = tableViewerColumn.getColumn();
		tblclmn_devId.setWidth(100);
		tblclmn_devId.setText("设备Id");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmn_devMac = tableViewerColumn_1.getColumn();
		tblclmn_devMac.setWidth(100);
		tblclmn_devMac.setText("设备Mac");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmn_devMac2 = tableViewerColumn_2.getColumn();
		tblclmn_devMac2.setWidth(100);
		tblclmn_devMac2.setText("设备Mac2");

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn = tableViewerColumn_3.getColumn();
		tableColumn.setWidth(100);
		tableColumn.setText("设备名称");

		TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_3 = tableViewerColumn_6.getColumn();
		tableColumn_3.setWidth(100);
		tableColumn_3.setText("设备类型");

		TableViewerColumn tableViewerColumn_7 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmnSn = tableViewerColumn_7.getColumn();
		tblclmnSn.setWidth(100);
		tblclmnSn.setText("SN号");

		TableViewerColumn tableViewerColumn_8 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmnip = tableViewerColumn_8.getColumn();
		tblclmnip.setWidth(100);
		tblclmnip.setText("设备IP");

		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_2 = tableViewerColumn_5.getColumn();
		tableColumn_2.setWidth(100);
		tableColumn_2.setText("注册时间");

		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tableColumn_1 = tableViewerColumn_4.getColumn();
		tableColumn_1.setWidth(100);
		tableColumn_1.setText("绑定用户");

		Menu menu = new Menu(table);
		table.setMenu(menu);

		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetDeviceOwner();
				doGetDevices();
			}
		});
		menuItem.setText("清除用户绑定");
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setContentProvider(new ContentProvider());

	}

	protected void resetDeviceOwner() {
		TableItem[] selectItems = table.getSelection();
		for (TableItem selectItem : selectItems) {
			Object data = selectItem.getData();

			if (data instanceof UserVO) {
				IDeviceService deviceService = new DeviceServiceImpl();
				String deviceId = ((DeviceVO) data).getId();
				System.out.println("reset device owner, deviceId= " + deviceId);
				deviceService.resetDeviceOwner(deviceId);
				deviceService.destroy();
			}
		}
	}

	protected void doGetDevices() {
		try {
			IDeviceService deviceService = new DeviceServiceImpl();
			List<DeviceVO> devices = deviceService.getDevices(text.getText());
			tableViewer.setInput(devices);
			deviceService.destroy();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
